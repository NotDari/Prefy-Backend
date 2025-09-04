package com.daribear.PrefyBackend.CurrentVote;

import com.daribear.PrefyBackend.Posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The repository which handles the current vote and all functions related to it.
 */
@Repository
public interface CurrentVoteRepository extends JpaRepository<CurrentVote, Long> {

    /**
     * Attempts to find a current vote for a specific user for a specific post.
     * @param userId id of user to get the vode from
     * @param postId id of post to get the vote from
     * @return the optional current vote if it exists
     */
    @Query("SELECT v FROM CurrentVote v WHERE v.userId = ?1 AND v.postId = ?2")
    Optional<CurrentVote> findCurrentVote(Long userId, Long postId);

    /**
     * Gets a list of votes from a user id in a list of posts
     * @param userId user id to get votes from
     * @param voteList list of posts to get votes from
     * @return List of Votes
     */
    @Query("SELECT v FROM CurrentVote v WHERE v.userId = :userId AND v.postId in :votes")
    Optional<ArrayList<CurrentVote>> findCurrentVoteList(@Param("userId") Long userId , @Param("votes") ArrayList<Long> voteList);

    /**
     * Updates a vote into a new vote
     * @param voteId id of vote to update
     * @param vote new vote status
     * @return successful
     */
    @Transactional
    @Modifying
    @Query("UPDATE CurrentVote v " +
            "SET v.currentVote = :Vote WHERE v.voteId = :VoteID")
    int updateCurrentVoteVote(@Param("VoteID") Long voteId, @Param("Vote") String vote);
}

