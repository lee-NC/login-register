package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.School;
import com.jasu.loginregister.Entity.Tutor;

public interface SchoolService {

    School findBySchoolID(Long schoolId);

    void updateSchool(School school);

    void createSchool(School school);

    boolean existedByTutorAndSchoolName(Tutor tutor, String schoolName);

    void deleteSchool(School school);
}
