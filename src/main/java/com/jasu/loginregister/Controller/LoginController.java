package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Jwt.JwtResponse;
import com.jasu.loginregister.Jwt.JwtUtils;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Model.Request.LogOutRequest;
import com.jasu.loginregister.Model.Request.TokenRefreshRequest;
import com.jasu.loginregister.Service.RefreshTokenService;
import com.jasu.loginregister.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.jasu.loginregister.Entity.DefineEntityStateMessage.*;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user){
        log.info("Login in Controller");

        User checkUser = userService.loginWithEmailAndPassword(user.getEmail(),user.getPassword());
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

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(token);
                })
                .orElseThrow(() -> new ForbiddenException("ACCESS DENIED"));
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
