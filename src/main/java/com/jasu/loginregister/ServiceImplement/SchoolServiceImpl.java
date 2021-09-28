package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Achievement;
import com.jasu.loginregister.Entity.School;
import com.jasu.loginregister.Entity.Tutor;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.SchoolRepository;
import com.jasu.loginregister.Service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;


    @Override
    public School findBySchoolID(Long schoolId) {
        Optional<School> school = schoolRepository.findById(schoolId);
        if (!school.isPresent()){
            throw new NotFoundException("No achievement found");
        }
        return school.get();
    }

    @Override
    public void updateSchool(School school) {
        try {
            schoolRepository.saveAndFlush(school);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createSchool(School school) {
        try {
            schoolRepository.saveAndFlush(school);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean existedByTutorAndSchoolName(Tutor tutor, String schoolName) {
        return schoolRepository.existsByTutorAndSchoolName(tutor,schoolName);
    }

    @Override
    public void deleteSchool(School school) {
        try {
            schoolRepository.delete(school);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
