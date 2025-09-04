package com.daribear.PrefyBackend.Comments;


import com.daribear.PrefyBackend.IncomeClasses.DeleteItemWrapper;
import com.daribear.PrefyBackend.IncomeClasses.IncomeCommentListById;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

/**
 * Controller handling the endpoints for all queries relating comments
 */
@RestController
@RequestMapping(path = "prefy/v1/Comments")
public class CommentController {
    private CommentService commentService;

    public CommentController() {
    }
    //Dependancy injection
    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    /**
     * Submits a new comment whether its a reply or an original comment to the repo
     * @param commentRequest class containing the details for writing the comment including the user id
     */
    @PostMapping("/SubmitComment")
    public void createNewComment(@RequestBody SubmitCommentRequest commentRequest){
        Comment comment = commentRequest.getComment();
        if (comment.getReplyCount() == null){
            comment.setReplyCount(0);
        }
        commentService.addNewComment(comment, commentRequest.getUserId());
    }

    /**
     * Get a page of replies to a comment.
     * @param commentId comment to get replies for
     * @param pageNumber pagination page number to get
     * @return A list(representing) a page of comments
     */
    @GetMapping("/GetCommentReplies")
    public List<Comment> getCommentReplies(@RequestParam Long commentId,Integer pageNumber ){
        return commentService.getCommentReplies(commentId, pageNumber);
    }

    /**
     * Get a list/page of comments for a post.
     * @param postId post to retrieve the comments for
     * @param pageNumber pagination page number to get
     * @return A list(representing) a page of comments
     */
    @GetMapping("/PostComments")
    public List<FullComment> getCommentsByPost(@RequestParam Long postId, Integer pageNumber){
        return commentService.getPostComments(postId, pageNumber);
    }

    /**
     * Delete a comment from the repository
     * @param deleteItemWrapper class containing the details of the comment to delete
     */
    @PutMapping("/DeleteComment")
    public void deleteComment(@RequestBody DeleteItemWrapper deleteItemWrapper){
        commentService.deleteComment(deleteItemWrapper.getItemId());
    }

}
