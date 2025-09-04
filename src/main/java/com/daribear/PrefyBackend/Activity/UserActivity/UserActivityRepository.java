package com.daribear.PrefyBackend.Activity.UserActivity;

import com.daribear.PrefyBackend.Authentication.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository for handling all of the actions surrounding the UserActivity Entity.
 */
@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {


    /**
     * Attempts to find a userActivity with the same id as the one provided.
     * @param id id of the userActivity to attempt to find
     * @return an optional user activity if it exists
     */
    Optional<UserActivity> findById(Long id);

    /**
     * Updates a userActivity, and modifies newActivitiesCount,newCommentscount,newVotescount and newFollowsCount.
     * @param id id of the user id to update
     * @param newActivitiesCount newActivitiesCount to replace the old one with
     * @param newCommentscount newCommentscount to replace the old one with
     * @param newVotescount newVotescount to replace the old one with
     * @param newFollowsCount newFollowsCount to replace the old one with
     * @return a 1 if successful, the 0 if not
     */
    @Transactional
    @Modifying
    @Query("UPDATE UserActivity a " +
            "SET a.newActivitiesCount = ?2 " +
            ", a.newCommentsCount = ?3 " +
            ", a.newVotesCount = ?4 " +
            ", a.newFollowsCount = ?5 " +
            "WHERE a.id = ?1 ")
    int updateUserActivity(Long id, Long newActivitiesCount, Long newCommentscount, Long newVotescount, Long newFollowsCount);

}
