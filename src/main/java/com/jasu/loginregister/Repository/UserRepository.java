package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Model.Dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmailAndPassword (String email, String Password);

    User findUserByEmail(String email);
}
