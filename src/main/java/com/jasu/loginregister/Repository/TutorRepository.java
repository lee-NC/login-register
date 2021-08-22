package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Tutor;
import com.jasu.loginregister.Model.Request.CreateUserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor,Long> {
    Tutor findByUser(CreateUserRequest createUserRequest);
}
