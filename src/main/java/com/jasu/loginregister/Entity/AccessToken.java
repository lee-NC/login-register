package com.jasu.loginregister.Entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "jasu_access_token")
@EntityListeners(AuditingEntityListener.class)
public class AccessToken  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "refresh_token_id", referencedColumnName = "id")
    private RefreshToken refreshToken;

    @Column(nullable = false, unique = true)
    private String accessToken;

    @Column(nullable = false)
    private Instant expiryDate;

    @CreatedDate
    @Column(nullable = false)
    private String createdAt;
}
