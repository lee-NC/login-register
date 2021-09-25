package com.jasu.loginregister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "jasu_class_tutor")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ClassTutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long classroomCtId;

    @Column(nullable = false)
    private Long userCtId;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private Boolean likeClass;

    @CreatedDate
    @Column(nullable = false)
    private String createdAt;

    @LastModifiedDate
    private String updatedAt;

    public ClassTutor(Long id) {
        this.id = id;
    }
}
