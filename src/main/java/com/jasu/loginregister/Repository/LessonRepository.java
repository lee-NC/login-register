package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson,Long> {
    boolean existsByBeginTimeAndEndTimeAndDayOfWeek(String beginTime, String endTime, String dayOfWeek);
}
