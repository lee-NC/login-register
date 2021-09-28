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

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;


    @Override
    public School findBySchoolID(Long schoolId) {
        log.info("Find school in Service");
        Optional<School> school = schoolRepository.findById(schoolId);
        if (!school.isPresent()){
            throw new NotFoundException("No school found");
        }
        return school.get();
    }

    @Override
    @Transactional
    public void deleteSchool(School school) {
        log.info("delete school in Service");
        try {
            schoolRepository.delete(school);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
