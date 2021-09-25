package com.jasu.loginregister.Entity;


import com.jasu.loginregister.Entity.DefinitionEntity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "jasu_student")
@Data
public class Student extends BaseEntity {

    @Column(nullable = false)
    private Long userStudentId;

    @Column(nullable = false)
    private int grade;

    @Column(nullable = false)
    private float assessment;

    @Column(nullable = false)
    private int numAssessment;

    public Student(Long id) {
        super(id);
    }

    public Student() {
        super();
    }


}
