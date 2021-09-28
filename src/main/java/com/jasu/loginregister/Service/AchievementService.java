package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Achievement;
import com.jasu.loginregister.Entity.Tutor;

public interface AchievementService {
    Achievement findById(Long achievementId);

    void updateAchievement(Achievement achievement);

    void createAchievement(Achievement achievement);

    boolean existedByTutorAndAchievement(Tutor tutor, String achievement);

    void deleteAchievement(Achievement achievement);
}
