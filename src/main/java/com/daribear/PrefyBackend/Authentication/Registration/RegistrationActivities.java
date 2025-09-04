package com.daribear.PrefyBackend.Authentication.Registration;

import com.daribear.PrefyBackend.Activity.UserActivity.UserActivity;
import com.daribear.PrefyBackend.Activity.UserActivity.UserActivityRepository;
import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Users.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsible for initializing activity tracking for new users.
 */
@AllArgsConstructor
@Service
public class RegistrationActivities {
    private UserActivityRepository userActivityRepo;

    /**
     * Initialize the activity tracking for a given user.
     * @param user user to initiate the activites for.
     */
    public void initActivities(User user){
        setUpActivities(user.getId());
    }

    /**
     * Creates and saves a UserActivity entity for the user with all counts set to zero.
     * @param id user id
     */
    private void setUpActivities(Long id){
        UserActivity userActivity = new UserActivity();
        userActivity.setId(id);
        userActivity.setNewActivitiesCount(0L);
        userActivity.setNewCommentsCount(0L);
        userActivity.setNewVotesCount(0L);
        userActivityRepo.saveAndFlush(userActivity);

    }
}
