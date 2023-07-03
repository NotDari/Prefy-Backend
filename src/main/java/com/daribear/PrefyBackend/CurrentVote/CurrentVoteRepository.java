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

@Repository
public interface CurrentVoteRepository extends JpaRepository<CurrentVote, Long> {

    @Query("SELECT v FROM CurrentVote v WHERE v.userId = ?1 AND v.postId = ?2")
    Optional<CurrentVote> findCurrentVote(Long userId, Long postId);


    @Query("SELECT v FROM CurrentVote v WHERE v.userId = :userId AND v.postId in :votes")
    Optional<ArrayList<CurrentVote>> findCurrentVoteList(@Param("userId") Long userId , @Param("votes") ArrayList<Long> voteList);


    @Transactional
    @Modifying
    @Query("UPDATE CurrentVote v " +
            "SET v.currentVote = :Vote WHERE v.voteId = :VoteID")
    int updateCurrentVoteVote(@Param("VoteID") Long voteId, @Param("Vote") String vote);
}

