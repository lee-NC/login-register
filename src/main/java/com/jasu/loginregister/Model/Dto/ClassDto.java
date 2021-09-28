package com.jasu.loginregister.Model.Dto;

import com.jasu.loginregister.Entity.Lesson;
import com.jasu.loginregister.Model.Dto.BasicDto.TutorDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ClassDto {

    private Long id;

    private int maxNum;

    private String type;

    private int currentNum;

    private Long fee;

    private int grade;

    private int numLesson;

    private String beginDay;

    private String subject;

    private Set<Lesson> lessonList;

}
