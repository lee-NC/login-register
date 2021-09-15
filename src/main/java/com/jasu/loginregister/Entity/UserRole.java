package com.jasu.loginregister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "jasu_user_role")
@Data
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String roleKey;

    public UserRole(Long userId, String roleKey) {
        this.userId = userId;
        this.roleKey = roleKey;
    }
}
