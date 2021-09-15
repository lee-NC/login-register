package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Repository.SchoolRepository;
import com.jasu.loginregister.Service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

}
