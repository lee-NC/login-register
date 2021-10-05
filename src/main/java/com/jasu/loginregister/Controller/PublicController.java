package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Jwt.JwtResponse;
import com.jasu.loginregister.Jwt.JwtUtils;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Model.Request.LoginRequest;
import com.jasu.loginregister.Model.Request.PaymentRequest;
import com.jasu.loginregister.Model.Request.TokenRefreshRequest;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
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
    private PaymentService paymentService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("Login in Controller");

        User checkUser = userService.loginWithEmailAndPassword(loginRequest.getEmail(),loginRequest.getPassword());
        UserPrincipal userPrincipal = UserMapper.toUserPrincipal(checkUser);
        if (refreshTokenService.checkTimeLogin(checkUser.getId().toString())){

            String jwt = jwtUtils.generateJwtToken(userPrincipal);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());
            accessTokenService.createAccessToken(defineAccessToken(refreshToken,jwt));
            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
                    checkUser.getFullName(),checkUser.getNumActive(),checkUser.getAvatar(), checkUser.getCoin()));
        }
        throw new ForbiddenException("ACCESS DENIED");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        log.info("Refresh token in Controller");
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken =  refreshTokenService.findByToken(requestRefreshToken);
        if(refreshTokenService.verifyExpiration(refreshToken) && !refreshToken.getDeleted()
            && refreshToken.getNumUses()<24   ){
            String token = jwtUtils.generateTokenFromUserId(refreshToken.getUser().getId());
            refreshToken.setNumUses(refreshToken.getNumUses()+1);
            refreshTokenService.updateRefreshToken(refreshToken);
            accessTokenService.createAccessToken(defineAccessToken(refreshToken,token));
            return ResponseEntity.ok("accessToken: "+token);
        }
        throw new ForbiddenException("Refresh token was expired. Please make a new login request");
    }

    @PutMapping("/logout/{id}")
    @PreAuthorize("hasAnyAuthority('USER') && (authentication.principal.id == #userId)")
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

    @PostMapping("/recharge/{id}")
    @PreAuthorize("hasAuthority('USER') && (authentication.principal.id == #userId)")
    @Secured("USER")
    public ResponseEntity<?> recharge(@PathVariable("id") Long  userId, @Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("Charge money in Controller");
        User user = userService.findByID(userId);
        if (user.getState().equals("LOGOUT")
//                ||!user.isEnabled()
        ){
            throw new ForbiddenException("ACCESS DENIED");
        }
        long coin = 0;
        if (paymentRequest.getFee()>10){
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            switch (paymentRequest.getCurrency()){
                case "VND":{
                    coin = paymentRequest.getFee()/100L;
                    break;
                }
                case "USD":{
                    coin = paymentRequest.getFee()/100L*22753L;
                    break;
                }
                default:{
                    throw new NotFoundException("This currency is not supported");
                }
            }
            Payment payment = new Payment(paymentRequest.getFee(), paymentRequest.getCurrency(),
                    paymentRequest.getMethod(), paymentRequest.getIntent(),formatter.format(new Date()),userId);
            paymentService.createPayment(payment);

            user.setCoin(user.getCoin()+coin);
            userService.updateUser(user);
            return ResponseEntity.ok(ACTION_SUCCESSFULLY);
        }
        return ResponseEntity.badRequest().body(ACTION_UNSUCCESSFULLY);
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
