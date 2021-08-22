package com.jasu.loginregister.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTutorRequest {

    private CreateUserRequest createUserRequest;

    private String literacy;

    private String school;

    private String experience;

    private String achievement;
}
