package com.jasu.loginregister.Service;

import com.jasu.loginregister.Model.Dto.TutorDto;
import com.jasu.loginregister.Model.Request.CreateTutorRequest;

public interface TutorService {
    TutorDto createTutor(CreateTutorRequest createTutorRequest);
}
