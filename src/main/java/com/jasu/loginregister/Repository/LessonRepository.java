package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson,Long> {

    @Query(value = "SELECT jasu_lesson.id FROM jasu_classroom " +
            "INNER JOIN jasu_schedule ON jasu_classroom.id = jasu_schedule.classroom_schedule_id " +
            "INNER JOIN jasu_lesson ON jasu_schedule.lesson_schedule_id = jasu_lesson.id " +
            "WHERE jasu_classroom.id = ?1 " +
            "AND jasu_lesson.begin_time = ?2 " +
            "AND jasu_lesson.end_time = ?3 " +
            "AND jasu_lesson.day_of_week = ?4 ",
    nativeQuery = true)
    List<Long> checkExist(Long classId, String beginTime,
                          String endTime, String dayOfWeek);
}
