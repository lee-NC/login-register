package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.CreatedToUser.CreateTutorRequest;
import com.jasu.loginregister.Repository.TutorRepository;
import com.jasu.loginregister.Service.TutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class TutorServiceImpl implements TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public Tutor createTutor(CreateTutorRequest createTutorRequest) {
        log.info("Registry Tutor in Service");

        Tutor checkTutor = tutorRepository.findByUserTutorId(createTutorRequest.getUserId());
        if(checkTutor!=null){
            throw new DuplicateRecordException("This user was a tutor");
        }
        Tutor tutor = UserMapper.toTutor(createTutorRequest);
        Tutor result = new Tutor();
        try {
            result = tutorRepository.saveAndFlush(tutor);
        }catch (Exception e){
            log.info(e.getMessage());
        }

        return result;
    }

    @Override
    public Tutor findByUserId(Long userId) {
        Tutor checkTutor = tutorRepository.findByUserTutorId(userId);
        if(checkTutor==null){
            throw new NotFoundException("No tutor found");
        }
        return checkTutor;
    }


    @Override
    public List<Tutor> getByListUserId(List<Long> userIds) {
        List<Tutor> tutors = new ArrayList<>();
        try {
            for (Long userId: userIds){
                Tutor tutor = tutorRepository.findByUserTutorId(userId);
                if (tutor!=null){
                    tutors.add(tutor);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return tutors;
    }

    @Override
    public Tutor updateTutor(Tutor updateTutor) {
        Tutor tutor = new Tutor();
        try {
            tutor =  tutorRepository.saveAndFlush(updateTutor);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return tutor;
    }

    @Override
    public List<Tutor> search(String keyWord) {
        return null;
    }

}
