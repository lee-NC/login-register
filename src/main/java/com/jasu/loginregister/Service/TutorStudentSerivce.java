package com.jasu.loginregister.Service;

import java.util.List;

public interface TutorStudentSerivce {
    boolean createListStudentService(Long classId, Long tutorId, List<Long> studentIds);
}
