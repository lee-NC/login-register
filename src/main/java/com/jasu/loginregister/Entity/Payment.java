package com.jasu.loginregister.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

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

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private String createdAt;

    @Column(nullable = false,updatable = false)
    private Long createdBy;

    public Payment(Long fee, String currency, String method, String intent, String createdAt, Long createdBy) {
        this.fee = fee;
        this.currency = currency;
        this.method = method;
        this.intent = intent;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
