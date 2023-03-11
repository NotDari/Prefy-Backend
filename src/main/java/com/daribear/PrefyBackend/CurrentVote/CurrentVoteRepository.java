package com.daribear.PrefyBackend.CurrentVote;

import com.daribear.PrefyBackend.Posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrentVoteRepository extends JpaRepository<CurrentVote, Long> {

    @Query("SELECT v FROM CurrentVote v WHERE v.userId = ?1 AND v.postId = ?2")
    Optional<CurrentVote> findCurrentVote(Long userId, Long postId);
}

