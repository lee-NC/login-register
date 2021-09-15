package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.ErrorResponse;
import com.jasu.loginregister.Jwt.JwtUtil;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Model.Request.LogOutRequest;
import com.jasu.loginregister.Model.Request.LoginRequest;
import com.jasu.loginregister.Model.Request.TokenRefreshRequest;
import com.jasu.loginregister.Service.TokenService;
import com.jasu.loginregister.Service.UserRoleService;
import com.jasu.loginregister.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.jasu.loginregister.Entity.DefineEntityStateMessage.*;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user){
        log.info("Login in Controller");
        User checkUser = userService.loginWithEmailAndPassword(user.getEmail(),user.getPassword());
        if (tokenService.checkTimeLogin(checkUser.getId().toString())){
            UserPrincipal userPrincipal = UserMapper.toUserPrincipal(checkUser);
            Token token = new Token();
            token.setToken(jwtUtil.generateToken(userPrincipal));
            token.setTokenExpDate(jwtUtil.generateExpirationDate());
            token.setCreatedBy(userPrincipal.getId().toString());
            tokenService.deleteAllOldToken(checkUser.getId().toString());
            tokenService.createToken(token);
            return ResponseEntity.ok(token.getToken());
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.FORBIDDEN,"You login so many time, please check it later"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        UserPrincipal user = null;
        Token token = null;

        if (StringUtils.hasText(requestRefreshToken) && requestRefreshToken.startsWith("Token ")) {
            String jwt = requestRefreshToken.substring(6);
            user = jwtUtil.getUserFromToken(jwt);
            token = tokenService.findByToken(jwt);
        }
        if (null != user
                && null != token
                && token.getTokenExpDate().compareTo(new Date())<1
                && !token.getDeleted()
                && user.getId().equals(Long.parseLong(token.getCreatedBy()))) {
            token.setToken(jwtUtil.generateToken(user));
            token.setTokenExpDate(jwtUtil.generateExpirationDate());
            token.setCreatedBy(user.getId().toString());
            tokenService.deleteAllOldToken(user.getId().toString());
            tokenService.createToken(token);
            return ResponseEntity.ok(token.getToken());
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.FORBIDDEN,"ACCESS DENIED"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
       tokenService.deleteAllOldToken(logOutRequest.getUserId().toString());
        return ResponseEntity.ok(ACTION_APPLY_SUCCESSFUL);
    }
}
