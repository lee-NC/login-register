package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Achievement;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.AchievementRepository;
import com.jasu.loginregister.Service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Override
    public Achievement findById(Long achievementId) {
        log.info("Find achievement in Service");
        Optional<Achievement> achievement = achievementRepository.findById(achievementId);
        if (!achievement.isPresent()){
            throw new NotFoundException("No achievement found");
        }
        return achievement.get();
    }

    @Transactional
    @Override
    public void deleteAchievement(Achievement achievement) {
        log.info("Delete achievement in Service");
        try {
            achievementRepository.delete(achievement);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean checkExist(Achievement achievement) {
        log.info("check achievement exists in Service");
        boolean checkExist = achievementRepository.existsByTutorAndAchievementAndYear(achievement.getTutor(), achievement.getAchievement(), achievement.getYear());
        if (checkExist){
            return false;
        }
        return true;
    }

}
