package com.jasu.loginregister.Entity;

import com.jasu.loginregister.Entity.DefinitionEntity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "jasu_user_bank_account")
@Data
@NoArgsConstructor
public class UserBankAccount extends BaseEntity {

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
