package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.ErrorResponse;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Jwt.JwtResponse;
import com.jasu.loginregister.Jwt.JwtUtil;
import com.jasu.loginregister.Jwt.JwtUtils;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Model.Request.LogOutRequest;
import com.jasu.loginregister.Model.Request.LoginRequest;
import com.jasu.loginregister.Model.Request.TokenRefreshRequest;
import com.jasu.loginregister.Service.RefreshTokenService;
import com.jasu.loginregister.Service.TokenService;
import com.jasu.loginregister.Service.UserRoleService;
import com.jasu.loginregister.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jasu.loginregister.Entity.DefineEntityStateMessage.*;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private TokenService tokenService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user){
        log.info("Login in Controller");
        User checkUser = userService.loginWithEmailAndPassword(user.getEmail(),user.getPassword());
        UserPrincipal userPrincipal = UserMapper.toUserPrincipal(checkUser);

        if (tokenService.checkTimeLogin(checkUser.getId().toString())){
            String jwt = jwtUtils.generateJwtToken(userPrincipal);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userPrincipal.getId(),
                    checkUser.getFullName(),checkUser.getNumActive(),checkUser.getAvatar()));
        }
        return ResponseEntity.badRequest().body(new ForbiddenException("ACCESS DENIED"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
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

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
       tokenService.deleteAllOldToken(logOutRequest.getUserId().toString());
        return ResponseEntity.ok(ACTION_APPLY_SUCCESSFUL);
    }
}
