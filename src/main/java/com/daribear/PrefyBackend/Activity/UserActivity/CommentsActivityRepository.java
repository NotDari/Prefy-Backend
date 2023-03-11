package com.daribear.PrefyBackend.Activity.UserActivity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentsActivityRepository extends JpaRepository<CommentsActivity, Long> {




    Optional<CommentsActivity> findById(Long id);

    @Query("Select c FROM CommentsActivity c WHERE c.userId = ?1")
    Optional<List<CommentsActivity>> findCommentsActivityListById(Long id, Pageable pageable);

}
