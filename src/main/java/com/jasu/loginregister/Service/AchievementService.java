package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Achievement;

public interface AchievementService {
    Achievement findById(Long achievementId);

    void deleteAchievement(Achievement achievement);

    boolean checkExist(Achievement achievement);

}
