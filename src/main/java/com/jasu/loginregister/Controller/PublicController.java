package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Email.EmailService;
import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Jwt.JwtResponse;
import com.jasu.loginregister.Jwt.JwtUtils;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Model.Request.LoginRequest;
import com.jasu.loginregister.Model.Request.TokenRefreshRequest;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;

@RestController
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user){
        log.info("Login in Controller");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            User checkUser = userService.loginWithEmailAndPassword(user.getEmail(),user.getPassword());
            UserPrincipal userPrincipal = UserMapper.toUserPrincipal(checkUser);
            if (refreshTokenService.checkTimeLogin(checkUser.getId().toString())){

                String jwt = jwtUtils.generateJwtToken(userPrincipal);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());
                accessTokenService.createAccessToken(defineAccessToken(refreshToken,jwt));
                return ResponseEntity.ok(new JwtResponse(jwt,checkUser.getId(), refreshToken.getToken(),
                        checkUser.getFullName(),checkUser.getNumActive(),checkUser.getAvatar(), checkUser.getCoin()));
            }
        }
        throw new ForbiddenException("ACCESS DENIED");
    }

    @RequestMapping("/hello")
    public String welcome() {
        return "hello";
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        log.info("Refresh token in Controller");
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken =  refreshTokenService.findByToken(requestRefreshToken);
        if(refreshTokenService.verifyExpiration(refreshToken) && !refreshToken.getDeleted()
            && refreshToken.getNumUses()<24
            && refreshToken.getDelayTime().before(new Date())){
            String token = jwtUtils.generateTokenFromUserId(refreshToken.getUser().getId());
            refreshToken.setNumUses(refreshToken.getNumUses()+1);
            refreshTokenService.updateRefreshToken(refreshToken);
            accessTokenService.createAccessToken(defineAccessToken(refreshToken,token));
            return ResponseEntity.ok("accessToken: "+token);
        }
        throw new ForbiddenException("Refresh token was expired. Please make a new login request");
    }

    @PutMapping("/logout/{id}")
    @PreAuthorize("hasAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured("USER")
    public ResponseEntity<?> logoutUser(@PathVariable("id") Long  userId) {
        log.info("Logout in Controller");
        User user = userService.findByID(userId);
        if (user.getState().equals("LOGOUT")){
            throw new ForbiddenException("You logged out before.Do you want to log in?");
        }
        refreshTokenService.deleteByUserId(userId);

        return ResponseEntity.ok("Log out successful!");
    }

    @GetMapping("/reset_password")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email){
        log.info("Get new OTP in Controller");
        User saveUser = userService.findByEmail(email);

        if (saveUser.getNumGetOTP()<MAX_FAILED_LOGIN){
            String encodedOTP = RandomString.make(8);
            saveUser.setOneTimePassword(encodedOTP);
            saveUser.setOtpRequestTime(new Date(new Date().getTime() + OTP_TIME_TRACKING));
            saveUser.setNumGetOTP(1);
            userService.updateUser(saveUser);

            VERIFICATION_CONTENT = VERIFICATION_CONTENT + encodedOTP;
            emailService.sendAnEmail(saveUser.getEmail(),VERIFICATION_CONTENT,VERIFICATION_SUBJECT);
            return ResponseEntity.ok("Check your email to verify that this is you.");
        }
        return ResponseEntity.ok(ACTION_UNSUCCESSFULLY);
    }

    @GetMapping("/get_OTP")
    public ResponseEntity<?> getNewOTP(@RequestParam("email") String email){
        log.info("Get new OTP in Controller");
        User saveUser = userService.findByEmail(email);

        if (saveUser.getNumGetOTP()<MAX_FAILED_LOGIN){
            String encodedOTP = RandomString.make(8);
            saveUser.setOneTimePassword(encodedOTP);
            saveUser.setOtpRequestTime(new Date(new Date().getTime() + OTP_TIME_TRACKING));
            saveUser.setNumGetOTP(saveUser.getNumGetOTP()+1);
            userService.updateUser(saveUser);

            VERIFICATION_CONTENT = VERIFICATION_CONTENT + encodedOTP;
            emailService.sendAnEmail(saveUser.getEmail(),VERIFICATION_CONTENT,VERIFICATION_SUBJECT);
            return ResponseEntity.ok("Check your email to verify your action.");
        }
        return ResponseEntity.ok(ACTION_UNSUCCESSFULLY);
    }

    @GetMapping("/verify_registry")
    public ResponseEntity<?> verifyUserRegistry(@RequestParam("code") String code) {

        log.info("Verify email");
        User checkUser = userService.verifyUserRegistry(code);
        UserPrincipal userPrincipal = UserMapper.toUserPrincipal(checkUser);
        String jwt = jwtUtils.generateJwtToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());
        accessTokenService.createAccessToken(defineAccessToken(refreshToken,jwt));
        return ResponseEntity.ok(new JwtResponse(jwt,checkUser.getId(), refreshToken.getToken(),
                checkUser.getFullName(),checkUser.getNumActive(),checkUser.getAvatar(), checkUser.getCoin()));
    }

    @GetMapping("/verify_password")
    public ResponseEntity<?> verifyUserPassword(@RequestParam("code") String code,
                                                @RequestParam("password") String password) {
        User user = userService.verifyUserRegistry(code);
        if (user!=null){
            String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
            user.setPassword(hash);
            userService.updateUser(user);
            emailService.sendAnEmail(user.getEmail(),"You have change password at "+ new Date(),"Change password already");

            UserPrincipal userPrincipal = UserMapper.toUserPrincipal(user);
            if (refreshTokenService.checkTimeLogin(user.getId().toString())
                    &&refreshTokenService.checkNearLoginTime(user.getId().toString())){

                String jwt = jwtUtils.generateJwtToken(userPrincipal);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());
                accessTokenService.createAccessToken(defineAccessToken(refreshToken,jwt));
                return ResponseEntity.ok(new JwtResponse(jwt,user.getId(), refreshToken.getToken(),
                        user.getFullName(),user.getNumActive(),user.getAvatar(), user.getCoin()));
            }
        }
        return ResponseEntity.ok(ACTION_UNSUCCESSFULLY);
    }

    private AccessToken defineAccessToken(RefreshToken refreshToken, String jwt){
        AccessToken accessToken = new AccessToken();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        accessToken.setRefreshToken(refreshToken);
        accessToken.setExpiryDate(Instant.now().plusMillis(TIME_PER_HOUR));
        accessToken.setAccessToken(jwt);
        accessToken.setCreatedAt(formatter.format(date));

        return accessToken;
    }

}
