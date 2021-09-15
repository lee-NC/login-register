package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Subject;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.SubjectRepository;
import com.jasu.loginregister.Service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public Subject checkBySubjectName(String subject) {
        log.info("Check subject name is "+subject);
        Subject checkSubject = subjectRepository.findBySubjectName(subject);
        if (checkSubject==null){
            throw new NotFoundException("No subject found ");
        }
        return checkSubject;
    }
}
