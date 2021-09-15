package com.jasu.loginregister.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "jasu_lesson")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String beginTime;

    @Column(nullable = false)
    private String endTime;

    @Column(nullable = false)
    private String dayOfWeek;

    public Lesson(String beginTime, String endTime, String dayOfWeek) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }
}
