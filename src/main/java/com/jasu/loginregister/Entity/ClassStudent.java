package com.jasu.loginregister.Entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "jasu_class_student")
@NoArgsConstructor
public class ClassStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id",referencedColumnName = "id",updatable = false)
    private Classroom classroom;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id",referencedColumnName = "id",updatable = false)
    private Student student;

    private String state;

    private Boolean likeClass;

    public ClassStudent(Long id) {
        this.id = id;
    }


}
