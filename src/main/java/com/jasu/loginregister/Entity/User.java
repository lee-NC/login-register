package com.jasu.loginregister.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jasu_user")
@Data
@NoArgsConstructor
public class User extends BaseEntity {

    public User(Long id) {
        super(id);
    }

    private String fullName;

    private String email;

    private String password;

    private String gender;

    private String address;

    private Date birthday;

    private String phoneNumber;

    private Long coin;

    private String state;

    private String avatar;

    private String lastLogin;

    private String lastActive;

    private int numActive;

    private String roleKey;

}
