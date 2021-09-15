package com.jasu.loginregister.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "jasu_user_bank_account")
@Data
@NoArgsConstructor
public class UserBankAccount extends BaseEntity{

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private Long userAccountId;

    @Column(nullable = false)
    private String typeBankAccount;

    public UserBankAccount(Long accountId, Long userAccountId, String typeBankAccount) {
        this.accountId = accountId;
        this.userAccountId = userAccountId;
        this.typeBankAccount = typeBankAccount;
    }
}
