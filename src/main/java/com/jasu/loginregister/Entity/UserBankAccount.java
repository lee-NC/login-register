package com.jasu.loginregister.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "jasu_user_bank_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBankAccount extends BaseEntity{

    private Long accountId;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id",updatable = false)
    private User user = new User();

    private String typeBankAccount;



}
