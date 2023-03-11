package com.daribear.PrefyBackend.Activity.UserActivity;

import com.daribear.PrefyBackend.Authentication.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {




    Optional<UserActivity> findById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE UserActivity a " +
            "SET a.newActivitiesCount = ?2 " +
            ", a.newCommentsCount = ?3 " +
            ", a.newVotesCount = ?4 " +
            "WHERE a.id = ?1 ")
    int updateUserActivity(Long id, Long newActivitiesCount, Long newCommentscount, Long newVotescount);

}
