package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Jwt.JwtResponse;
import com.jasu.loginregister.Jwt.JwtUtils;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Model.Request.LoginRequest;
import com.jasu.loginregister.Model.Request.TokenRefreshRequest;
import com.jasu.loginregister.Service.ClassroomService;
import com.jasu.loginregister.Service.RefreshTokenService;
import com.jasu.loginregister.Service.TutorService;
import com.jasu.loginregister.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    AuthenticationManager authenticationManager;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private TutorService tutorService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("Login in Controller");

        User checkUser = userService.loginWithEmailAndPassword(loginRequest.getEmail(),loginRequest.getPassword());
        UserPrincipal userPrincipal = UserMapper.toUserPrincipal(checkUser);
        if (refreshTokenService.checkTimeLogin(checkUser.getId().toString())){

            String jwt = jwtUtils.generateJwtToken(userPrincipal);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userPrincipal.getId(),
                    checkUser.getFullName(),checkUser.getNumActive(),checkUser.getAvatar(), checkUser.getCoin()));
        }
        return ResponseEntity.badRequest().body("ACCESS DENIED");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        log.info("Refresh token in Controller");
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken =  refreshTokenService.findByToken(requestRefreshToken);
        if(refreshTokenService.verifyExpiration(refreshToken)){
            String token = jwtUtils.generateTokenFromUsername(refreshToken.getUser().getEmail());
            return ResponseEntity.ok("accessToken: "+token);
        }
        return ResponseEntity.badRequest().body(new ForbiddenException("Refresh token was expired. Please make a new login request"));
    }

    @PutMapping("/logout/{id}")
    public ResponseEntity<?> logoutUser(@PathVariable("id") Long  userId) {
        log.info("Logout in Controller");
        User user = userService.findByID(userId);
        if (user.getState().equals("LOGOUT")){
            throw new ForbiddenException("You logged out before.Do you want to log in?");
        }
        user.setState("LOGOUT");
        userService.updateUser(user);
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok("Log out successful!");
    }

}
