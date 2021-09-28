package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Lesson;

public interface LessonService {
    void deleteLessonById(Long id);

    Lesson findById(Long id);

    void updateLesson(Lesson lesson);

    void createLesson(Lesson lesson);

    void deleteLesson(Lesson lesson);

    boolean existedByBeginTimeAndEndTimeAndDayOfWeek(String beginTime, String endTime, String dayOfWeek);
}
