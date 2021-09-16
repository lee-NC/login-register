package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Model.Dto.BasicDto.UserDto;
import com.jasu.loginregister.Model.Request.CreatedToUser.CreateUserRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateUserRequest;

import java.util.List;

public interface UserService {

    User loginWithEmailAndPassword(String email, String password);

    User findByID(Long userCreateId);

    List<User> getListUser(List<Long> userIds);

    UserDto createUser(CreateUserRequest createUserRequest);

    User updateUser(UpdateUserRequest req, Long id);

    String deleteUser(Long id);

    void updateUser(User checkUser);

    User findByEmail(String username);
}
