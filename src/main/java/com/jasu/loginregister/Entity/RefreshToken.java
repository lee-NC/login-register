package com.jasu.loginregister.Entity;

import com.jasu.loginregister.Entity.DefinitionEntity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "jasu_refresh_token")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne()
  @JoinColumn(name = "user_rf_id", referencedColumnName = "id")
  private User user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private int numUses;

  @Column(nullable = false)
  private Instant expiryDate;

  @Column(nullable = false)
  private Boolean deleted;

  @Column()
  private Date delayTime;

  @CreatedDate
  @Column(nullable = false)
  private String createdAt;

  private String createdBy;

  @LastModifiedDate
  private Date updatedAt;

}
