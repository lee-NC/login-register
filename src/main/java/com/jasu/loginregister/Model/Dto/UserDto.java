package com.jasu.loginregister.Model.Dto;

import com.jasu.loginregister.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;

    private String fullName;

    private String avatar;

    private int numActive;

    private String roleKey;
//    private Long coin;
}
