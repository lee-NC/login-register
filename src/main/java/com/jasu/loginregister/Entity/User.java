package com.jasu.loginregister.Entity;

import com.jasu.loginregister.Entity.DefinitionEntity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jasu_user")
@Data
@NoArgsConstructor
public class User extends BaseEntity {

    public User(Long id) {
        super(id);
    }

    @Column(nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, length = 40, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String gender;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
    private Address address;

    @Column(length = 11)
    private String birthday;

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

    @Column()
    private Boolean enabled;

    @Column()
    private String oneTimePassword;

    @Column()
    private Date otpRequestTime;

    @Column()
    private int numGetOTP;

    @Column(nullable = false)
    private Boolean changePassword;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
