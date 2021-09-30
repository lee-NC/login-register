package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Entity.Lesson;

public interface LessonService {

    Lesson findById(Long id);

    void deleteLesson(Lesson lesson);

    boolean checkExist(Long classId,Lesson lesson);
}
