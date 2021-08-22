package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Model.Dto.UserDto;
import com.jasu.loginregister.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user){
        log.info("Login in Controller");
        UserDto result = userService.findUserByEmailAndPassword(user.getEmail(),user.getPassword());
        return ResponseEntity.ok(result);
    }
}
