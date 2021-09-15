package com.jasu.loginregister.Entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity(name = "jasu_class_student")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ClassStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long classroomCsId;

    @Column(nullable = false)
    private Long userCsId;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private Boolean likeClass;

    @CreatedDate
    @Column(nullable = false)
    private String createdAt;

    @LastModifiedDate
    private String updatedAt;

    public ClassStudent(Long id) {
        this.id = id;
    }




}
