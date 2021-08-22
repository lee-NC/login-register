package com.jasu.loginregister.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jasu_staff")
@Data
@NoArgsConstructor
public class Staff extends BaseEntity{

    private String fullName;

    private String email;

    private String password;

    private String roleKey;


    public Staff(Long id) {
        super(id);
    }
}
