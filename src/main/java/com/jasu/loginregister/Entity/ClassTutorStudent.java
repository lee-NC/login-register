package com.jasu.loginregister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "jasu_class_tutor_student")
@NoArgsConstructor
public class ClassTutorStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id",referencedColumnName = "id",updatable = false)
    private Classroom classroom;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_student_id",referencedColumnName = "id",updatable = false)
    private TutorStudent tutorStudent;

    private String state;

    public ClassTutorStudent(Long id) {
        this.id = id;
    }


}
