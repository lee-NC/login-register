package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Achievement;
import com.jasu.loginregister.Entity.Tutor;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.AchievementRepository;
import com.jasu.loginregister.Service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Override
    public Achievement findById(Long achievementId) {
        Optional<Achievement> achievement = achievementRepository.findById(achievementId);
        if (!achievement.isPresent()){
            throw new NotFoundException("No achievement found");
        }
        return achievement.get();
    }

    @Override
    public void updateAchievement(Achievement achievement) {
        try {
            achievementRepository.saveAndFlush(achievement);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createAchievement(Achievement achievement) {
        try {
            achievementRepository.saveAndFlush(achievement);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean existedByTutorAndAchievement(Tutor tutor, String achievement) {
        return achievementRepository.existsByTutorAndAchievement(tutor,achievement);
    }

    @Override
    public void deleteAchievement(Achievement achievement) {
        try {
            achievementRepository.delete(achievement);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
