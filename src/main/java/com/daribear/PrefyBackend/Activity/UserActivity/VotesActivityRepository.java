package com.daribear.PrefyBackend.Activity.UserActivity;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Comments.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface VotesActivityRepository extends JpaRepository<VotesActivity, Long> {




    Optional<VotesActivity> findById(Long id);

    @Query("Select v FROM VotesActivity v WHERE v.userId = ?1")
    Optional<List<VotesActivity>> findVotesActivityListById(Long id, Pageable pageable);

    boolean existsVotesActivityByPostId(Long postId);

    @Transactional
    @Modifying
    @Query("UPDATE Authentication a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAuthentication(String email);

    @Transactional
    @Modifying
    @Query("Update VotesActivity v " +
            "SET v.lastUserId = :lastUserId " +
            ", v.lastVoteDate = :lastVoteTime " +
            "WHERE v.postId = :postId")
    int updateVoteActivity(@Param("postId")Long postId,@Param("lastUserId") Long lastUserId, @Param("lastVoteTime") Double lastVoteTime);

}
