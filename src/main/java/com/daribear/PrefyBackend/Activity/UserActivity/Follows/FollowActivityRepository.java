package com.daribear.PrefyBackend.Activity.UserActivity.Follows;

import com.daribear.PrefyBackend.Activity.UserActivity.Votes.VotesActivity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Repository
public interface FollowActivityRepository extends JpaRepository<FollowActivity, Long> {


    @Query("Select f FROM FollowActivity f WHERE f.userId = ?1 AND f.followerId = ?2")
    Optional<FollowActivity> findIfExists(Long userId, Long followerId);

    @Query("Select f FROM FollowActivity f WHERE f.userId = ?1 AND f.followed = 1")
    Optional<List<FollowActivity>> findFollowersActivityListById(Long id, Pageable pageable);
}
