package com.jasu.loginregister.Entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "jasu_bank_information")
@Data
public class BankInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bankName;

    @Column (nullable = false)
    private String bankAvatar;
}
