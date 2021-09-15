package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Repository.AchievementRepository;
import com.jasu.loginregister.Service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

}
