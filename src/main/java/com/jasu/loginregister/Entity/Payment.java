package com.jasu.loginregister.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "jasu_payment")
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,updatable = false)
    private Long fee;

    @Column(nullable = false,updatable = false)
    private String currency;

    @Column(nullable = false,updatable = false)
    private String method;

    @Column(nullable = false,updatable = false)
    private String intent;

    @Column(nullable = false,updatable = false)
    private Long coin;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private String createdAt;

    @Column(nullable = false,updatable = false)
    private Long createdBy;

    @Column(nullable = false)
    private boolean isSuccess;

    @Column()
    private  String token;

    @Column()
    private Date expiryTime;

    @Column()
    private int numGetToken;

    public Payment(Long fee, String currency, String method, String intent, Long coin, String createdAt, Long createdBy, boolean isSuccess, String token, Date expiryTime, int numGetToken) {
        this.fee = fee;
        this.currency = currency;
        this.method = method;
        this.intent = intent;
        this.coin = coin;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.isSuccess = isSuccess;
        this.token = token;
        this.expiryTime = expiryTime;
        this.numGetToken = numGetToken;
    }
}
