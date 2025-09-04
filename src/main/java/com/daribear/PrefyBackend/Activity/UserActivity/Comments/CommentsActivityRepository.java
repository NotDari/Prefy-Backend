package com.daribear.PrefyBackend.Activity.UserActivity.Comments;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * The repository which handles the retrieval/submission of commentActivites.
 */
@Repository
public interface CommentsActivityRepository extends JpaRepository<CommentsActivity, Long> {


    /**
     * Retrieves the comments activity by its id.
     * @param id the id of the CommentsActivity
     * @return optional of the Comments Activity
     */
    Optional<CommentsActivity> findById(Long id);

    /**
     * Retrieves the list of comments activity with the associated user id.
     *
     * @param id the user id for the retrieval the commentsActivity
     * @param pageable the page to get the commentsActivity
     * @return optional of the list of comments activity
     */
    @Query("Select c FROM CommentsActivity c WHERE c.userId = ?1")
    Optional<List<CommentsActivity>> findCommentsActivityListById(Long id, Pageable pageable);

}
