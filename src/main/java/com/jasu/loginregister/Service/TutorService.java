package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Tutor;
import com.jasu.loginregister.Model.Request.CreatedToUser.CreateTutorRequest;

import java.util.List;

public interface TutorService {
    Tutor createTutor(CreateTutorRequest createTutorRequest);

    Tutor findByUserId(Long userId);

    List<Tutor> getByListUserId(List<Long> userIds);

    Tutor updateTutor(Tutor updateTutor);

    List<Tutor> search(String keyWord);
}
