package com.daribear.PrefyBackend.Activity.UserActivity.Follows;

import com.daribear.PrefyBackend.Activity.UserActivity.Votes.VotesActivity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


/**
 * The repository which handles the retrieval/submission of FollowActivities.
 */
@Repository
public interface FollowActivityRepository extends JpaRepository<FollowActivity, Long> {

    /**
     * Finding a follow activity if it exists between two users (in one direction , so one follows another).
     * @param userId the id of the person following
     * @param followerId the id of the person being followed
     * @return optional of the Follow activity(whether it exists)
     */
    @Query("Select f FROM FollowActivity f WHERE f.userId = ?1 AND f.followerId = ?2")
    Optional<FollowActivity> findIfExists(Long userId, Long followerId);

    /**
     * Retrieves a list of ollow activity where the user is being followed
     * @param id the user id to check
     * @param pageable the pagination details
     * @return list of follow activities.
     */
    @Query("Select f FROM FollowActivity f WHERE f.userId = ?1 AND f.followed = 1")
    Optional<List<FollowActivity>> findFollowersActivityListById(Long id, Pageable pageable);
}
