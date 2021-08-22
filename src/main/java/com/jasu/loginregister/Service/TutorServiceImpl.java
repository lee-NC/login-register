package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Tutor;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Model.Dto.TutorDto;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.CreateTutorRequest;
import com.jasu.loginregister.Model.Request.CreateUserRequest;
import com.jasu.loginregister.Repository.TutorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TutorServiceImpl implements TutorService{

    @Autowired
    private TutorRepository tutorRepository;


    @Override
    public TutorDto createTutor(CreateTutorRequest createTutorRequest) {
        log.info("Registry Tutor in Service");

////         Check user exist
//        CreateUserRequest createUserRequest = createTutorRequest.getCreateUserRequest();
//        Tutor result = tutorRepository.findByUser(createUserRequest);
//        if (result != null) {
//            throw new DuplicateRecordException("User is already in use");
//        }

        Tutor tutor = UserMapper.toTutor(createTutorRequest);
        tutorRepository.saveAndFlush(tutor);

        return UserMapper.toTutorDto(tutor);
    }
}
