package com.jasu.loginregister.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "jasu_token")
@Data
public class Token extends BaseEntity {

    @Column(length = 1000)
    private String token;

    @Column(nullable = false)
    private Date tokenExpDate;

}
