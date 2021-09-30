package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Entity.Lesson;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.LessonRepository;
import com.jasu.loginregister.Service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;


    @Override
    public Lesson findById(Long id) {
        log.info("find lesson in Service");
        Optional<Lesson> lesson = lessonRepository.findById(id);
        if (!lesson.isPresent()) {
            throw new NotFoundException("No lesson found");

        }
        return lesson.get();
    }

    @Transactional
    @Override
    public void deleteLesson(Lesson lesson) {
        log.info("delete lesson in Service");
        try {
            lessonRepository.delete(lesson);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean checkExist(Long classId, Lesson lesson) {
        log.info("check lesson exists in Service");
        List<Long> checkExist = lessonRepository.checkExist(classId, lesson.getBeginTime(),lesson.getEndTime(),lesson.getDayOfWeek());
        if (checkExist.size()>0){
            return false;
        }
        return true;
    }
}
