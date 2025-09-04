package com.daribear.PrefyBackend.Comments;


import com.daribear.PrefyBackend.Posts.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository representing the handling of the comment entity.
 */
@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {
    /**
     * Gets a comment based on the id provided.
     * @param id id of the comment to get
     * @return the comment if it exists.
     */
    @Query("SELECT c FROM Comment c WHERE c.id = ?1 AND c.deleted = 0")
    Optional<Comment> findCommentById(Long id);

    /**
     * Gets a list of parent comments based on the post id.
     * @param id id of the post
     * @param pageable pagination details
     * @return the list/page of comments
     */
    @Query("Select c FROM Comment c WHERE c.postId = ?1 AND c.parentId = null AND c.deleted = 0")
    Optional<List<Comment>> findCommentByPostId(Long id, Pageable pageable);

    /**
     * Finds a list/page of reply comments from the parent id
     * @param id the id of the parent comment
     * @param pageable the pagination details
     * @return list of reply comments
     */
    @Query("Select c FROM Comment c WHERE c.parentId = ?1 AND c.deleted = 0")
    Optional<List<Comment>> findCommentByParentId(Long id, Pageable pageable);

    /**
     * Finds a list/page of comments based on the pageable provided for a specific user
     * @param id user id
     * @param pageable pagination details
     * @return list(page) of comments
     */
    @Query("Select c FROM Comment c WHERE c.user = ?1 AND c.deleted = 0")
    Optional<List<Comment>> findCommentByUserId(Long id, Pageable pageable);

}
