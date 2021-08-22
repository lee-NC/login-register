package com.jasu.loginregister.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {

    private CreateUserRequest createUserRequest;

    private String literacy;
}
