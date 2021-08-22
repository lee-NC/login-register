package com.jasu.loginregister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean deleted;

    @CreatedDate
    private String createdAt;

    private String createdBy;

    @LastModifiedDate
    private String updatedAt;

    private String updatedBy;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public BaseEntity() {

    }
}
