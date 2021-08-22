package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.GlobalExceptionHandler;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Dto.UserDto;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.CreateUserRequest;
import com.jasu.loginregister.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public Boolean checkUser(String email) {
        log.info("Check User exist in Service");
        // Check email exist
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            throw new DuplicateRecordException("Email is already in use");
        }

        return true;
    }

    @Override
    public UserDto findUserByEmailAndPassword(String email, String password) {
        log.info("Login in Service");
        User result = userRepository.findByEmailAndPassword(email,password);
        if (result==null){
            throw new NotFoundException("No user found");
        }

        if (!result.getState().equals("ON")){
            result.setState("ON");
            userRepository.saveAndFlush(result);
        }

        return   UserMapper.toUserDto(result);
    }
}
