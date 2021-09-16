package com.jasu.loginregister.Entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity(name = "jasu_refresh_token")
public class RefreshToken extends BaseEntity{

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_rf_id", referencedColumnName = "id")
  private User user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

}
