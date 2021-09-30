package com.jasu.loginregister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.FilterDef;

import javax.persistence.*;

import static org.apache.coyote.http11.Constants.a;

@Entity
@Table(name = "jasu_tutor_achievement")
@Data
@NoArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutorId", referencedColumnName = "userTutorId")
    private Tutor tutor;

    @Column(nullable = false)
    private String achievement;

    @Column()
    private int year;

//    public Achievement(String achievement,int year) {
//        this.achievement = achievement;
//        this.year = year;
//    }

    public Achievement(Tutor tutor, String achievement, int year) {
        this.tutor = tutor;
        this.achievement = achievement;
        this.year = year;
    }
}
