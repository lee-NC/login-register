package com.jasu.loginregister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "jasu_tutor_school")
@Data
@NoArgsConstructor
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutorId", referencedColumnName = "userTutorId")
    private Tutor tutor;

    @Column(nullable = false)
    private String schoolName;

    public School(String schoolName) {
        this.schoolName = schoolName;
    }

    public School(Tutor tutor, String schoolName) {
        this.tutor = tutor;
        this.schoolName = schoolName;
    }
}
