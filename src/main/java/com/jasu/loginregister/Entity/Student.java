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

@Entity
@Table(name = "jasu_student")
@Data
public class Student extends BaseEntity{

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id",updatable = false)
    private User user;

    @Column(name = "literacy", nullable = false)
    private String literacy;

    @Column(name = "assessment")
    private float assessment;

    @Column()
    private int numAssessment;

    public Student(Long id) {
        super(id);
    }

    public Student() {
        super();
    }

}
