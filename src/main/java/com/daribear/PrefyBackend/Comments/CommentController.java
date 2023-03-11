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

@RestController
@RequestMapping(path = "prefy/v1/Comments")
public class CommentController {
    private CommentService commentService;

    public CommentController() {
    }

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/SubmitComment")
    public void createNewComment(@RequestBody SubmitCommentRequest commentRequest){
        Comment comment = commentRequest.getComment();
        if (comment.getReplyCount() == null){
            comment.setReplyCount(0);
        }
        commentService.addNewComment(comment, commentRequest.getUserId());
    }

    @GetMapping("/GetCommentReplies")
    public List<Comment> getCommentReplies(@RequestParam Long commentId,Integer pageNumber ){
        return commentService.getCommentReplies(commentId, pageNumber);
    }

    @GetMapping("/PostComments")
    public List<FullComment> getCommentsByPost(@RequestParam Long postId, Integer pageNumber){
        return commentService.getPostComments(postId, pageNumber);
    }

    @PutMapping("/DeleteComment")
    public void deleteComment(@RequestBody DeleteItemWrapper deleteItemWrapper){
        commentService.deleteComment(deleteItemWrapper.getItemId());
    }

}
