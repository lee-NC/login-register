package com.jasu.loginregister.Entity;

import com.jasu.loginregister.Entity.DefinitionEntity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "jasu_refresh_token")
public class RefreshToken extends BaseEntity {

  @OneToOne()
  @JoinColumn(name = "user_rf_id", referencedColumnName = "id")
  private User user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

}
