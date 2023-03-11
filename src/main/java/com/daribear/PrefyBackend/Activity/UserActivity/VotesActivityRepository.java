package com.daribear.PrefyBackend.Activity.UserActivity;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Comments.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface VotesActivityRepository extends JpaRepository<VotesActivity, Long> {




    Optional<VotesActivity> findById(Long id);

    @Query("Select v FROM VotesActivity v WHERE v.userId = ?1")
    Optional<List<VotesActivity>> findVotesActivityListById(Long id, Pageable pageable);

}
