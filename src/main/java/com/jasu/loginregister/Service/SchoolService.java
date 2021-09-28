package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.School;

public interface SchoolService {

    School findBySchoolID(Long schoolId);


    void deleteSchool(School school);
}
