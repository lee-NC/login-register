package com.jasu.loginregister.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "jasu_student")
@Data
public class Student extends BaseEntity{

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
