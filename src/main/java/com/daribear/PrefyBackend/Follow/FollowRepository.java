package com.daribear.PrefyBackend.Follow;


import com.daribear.PrefyBackend.Activity.UserActivity.Follows.FollowActivity;
import com.daribear.PrefyBackend.CurrentVote.CurrentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {


    @Query("Select f FROM Follow f WHERE f.userId = :followerId AND f.followerId = :userId")
    Optional<Follow> findIfExists(Long userId, Long followerId);


}
