package com.jasu.loginregister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "jasu_user")
@Data
@NoArgsConstructor
public class User extends BaseEntity {

    public User(Long id) {
        super(id);
    }

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String gender;

    private String address;

    private String birthday;

    private String phoneNumber;

    @Column(nullable = false)
    private Long coin;

    @Column(nullable = false)
    private String state;

    @Column()
    private String avatar;

    @Column(nullable = false)
    private String lastLogin;

    @Column(nullable = false)
    private String lastActive;

    @Column(nullable = false)
    private int numActive;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "jasu_user_roles",
            joinColumns = @JoinColumn(name = "user_ur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_ur_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
