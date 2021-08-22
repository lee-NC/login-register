package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Model.Dto.UserDto;
import com.jasu.loginregister.Model.Request.CreateUserRequest;

public interface UserService {
    public Boolean checkUser(String email);
    public UserDto findUserByEmailAndPassword(String email,String password);
}
