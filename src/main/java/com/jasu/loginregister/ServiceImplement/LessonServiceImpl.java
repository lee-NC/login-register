package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Lesson;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.LessonRepository;
import com.jasu.loginregister.Service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public void deleteLessonById(Long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        if (lesson==null){
            throw new NotFoundException("No lesson found");
        }
        lessonRepository.delete(lesson.get());
    }
}
