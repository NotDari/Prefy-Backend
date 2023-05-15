package com.daribear.PrefyBackend.Activity.UserActivity.Follows;

import com.daribear.PrefyBackend.Activity.UserActivity.Votes.VotesActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;


@Repository
public interface FollowActivityRepository extends JpaRepository<FollowActivity, Long> {


    @Query("Select f FROM FollowActivity f WHERE f.userId = ?1 AND f.followerId = ?2")
    Optional<FollowActivity> findIfExists(Long userId, Long followerId);
}
