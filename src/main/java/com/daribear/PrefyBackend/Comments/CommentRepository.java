package com.daribear.PrefyBackend.Comments;


import com.daribear.PrefyBackend.Posts.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.id = ?1")
    Optional<Comment> findCommentById(Long id);

    @Query("Select c FROM Comment c WHERE c.postId = ?1 AND c.parentId = null")
    Optional<List<Comment>> findCommentByPostId(Long id, Pageable pageable);

    @Query("Select c FROM Comment c WHERE c.parentId = ?1")
    Optional<List<Comment>> findCommentByParentId(Long id, Pageable pageable);

    @Query("Select c FROM Comment c WHERE c.user = ?1")
    Optional<List<Comment>> findCommentByUserId(Long id, Pageable pageable);

}
