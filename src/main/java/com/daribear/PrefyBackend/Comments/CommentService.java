package com.daribear.PrefyBackend.Comments;


import com.daribear.PrefyBackend.Activity.ActivityService;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostRepository;
import com.daribear.PrefyBackend.Users.UserRepository;
import com.daribear.PrefyBackend.Utils.CurrentTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service level which handles the submission/deletion of comments.
 */
@Service
@Component
public class CommentService {
    private CommentRepository commentRepo;
    private UserRepository userRepo;

    private PostRepository postRepo;
    private ActivityService activityService;


    public CommentService() {
    }

    //Dependancy injected constructor
    @Autowired
    public CommentService (CommentRepository commentRepo, UserRepository userRepo, PostRepository postRepo, ActivityService activityService){
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
        this.activityService = activityService;
    }

    /**
     * Add a new comment to the database.
     * Checks if the user exists,
     * also creates the comment activity for the given user.
     * @param comment comment to be added
     * @param userId user who made the comment
     */
    @Transactional
    public void addNewComment(Comment comment, Long userId){
        if (checkIfValid(comment)) {
            //Check user is valid
            if (userRepo.existsById(userId)){
                comment.setUser(userRepo.getById(userId));
                comment.setDeleted(false);
                //Add an extra comment count to the post
                Post post = postRepo.findPostById(comment.getPostId()).get();
                post.setCommentsNumber(post.getCommentsNumber() + 1);
                postRepo.save(post);
                activityService.madeComment(comment);
                commentRepo.save(comment);
            }
        }
    }

    /**
     * Checks if a reply comment is valid.
     * If its not a reply comment its returned true otherwise it checks and alters the required details.
     *
     * @param comment comment to be checked
     * @return whether or not its valid
     */
    private Boolean checkIfValid(Comment comment){
        if (comment.getParentId() == null){
            return true;
        } else {
            Comment parentComment = commentRepo.getById(comment.getParentId());
            //If parent comment has no existing comments, add 1 comment
            if (parentComment.getReplyCount() == null){
                parentComment.setReplyCount(1);
                commentRepo.saveAndFlush(parentComment);
                return true;
            }
            //Checking the parent comment doesn't have too many replies
            if (parentComment.getReplyCount() >= 500){
                return false;
            } else {
                //Increment the reply count
                parentComment.setReplyCount(parentComment.getReplyCount() + 1);
                commentRepo.saveAndFlush(parentComment);
                return true;
            }
        }
    }

    /**
     * Retrieves a page of top level comments to a post.
     * If the comment has replies, the first 10 replies are loaded in for each post aswell.
     *
     * @param postId the post to get the comments for
     * @param pageNumber the page of comments to retrieve
     * @return the list of comments that fit the criteria
     */
    public List<FullComment> getPostComments(Long postId, Integer pageNumber){
        Pageable mainCommentPageable = PageRequest.of(pageNumber, 10);
        Optional<List<Comment>> commentList = commentRepo.findCommentByPostId(postId, mainCommentPageable);
        List<FullComment> fullCommentList = new ArrayList<>();
        //If the post has comments
        if (commentList.isPresent()){
            for (Comment comment : commentList.get()){
                FullComment fullComment = new FullComment();
                fullComment.convertComment(comment);
                //If the comment has replies
                if (fullComment.getReplyCount() != null){
                    //If the comment has replies
                    if (fullComment.getReplyCount() > 0){
                        Pageable replyCommentPageable = PageRequest.of(0, 10);
                        Optional<List<Comment>> replyCommentList =  commentRepo.findCommentByParentId(fullComment.getId(), replyCommentPageable);
                        //If the comment does have valid replies
                        if (replyCommentList.isPresent()){
                            //Get the page of replies and add it to the comment so that it can be returned.
                            ArrayList<Comment> commentReplyList = new ArrayList<>(replyCommentList.get());
                            for (int i =0;i< commentReplyList.size(); i ++){
                                commentReplyList.get(i).setReplyUsername(fullComment.getUser().getUsername());
                                if (commentReplyList.get(i).getSubParentId() != null){
                                    Optional<Comment> subParentCommentOpt = commentRepo.findCommentById(commentReplyList.get(i).getSubParentId());
                                    if (subParentCommentOpt.isPresent()){
                                        commentReplyList.get(i).setReplyUsername(subParentCommentOpt.get().getUser().getUsername());
                                    }
                                }
                            }
                            fullComment.setReplyList(commentReplyList);
                        }
                    }
                }
                fullCommentList.add(fullComment);
            }
            return fullCommentList;
        }else {
            return null;
        }
    }

    /**
     * Get a page (of max 10) of the replies of a comment.
     *
     * @param commentId comment to get a reply of
     * @param pageNumber page number to get
     * @return the page of max 10 new comments
     */
    public List<Comment> getCommentReplies(Long commentId, Integer pageNumber){
        Pageable replyCommentPageable = PageRequest.of(pageNumber, 10);
        Optional<List<Comment>> replyCommentList =  commentRepo.findCommentByParentId(commentId, replyCommentPageable);
        //If the comment has replies
        if (replyCommentList.isPresent()){
            ArrayList<Comment> commentReplyList = new ArrayList<>(replyCommentList.get());
            //Loop through all comment replies
            for (int i =0;i< commentReplyList.size(); i ++){
                // Reply is to another reply (nested reply)
                if (commentReplyList.get(i).getSubParentId() != null){
                    Optional<Comment> subParentCommentOpt = commentRepo.findCommentById(commentReplyList.get(i).getSubParentId());
                    if (subParentCommentOpt.isPresent()){
                        commentReplyList.get(i).setReplyUsername(subParentCommentOpt.get().getUser().getUsername());
                    }
                } else {
                    // Reply is directly to the parent comment
                    Optional<Comment> parentComment = commentRepo.findCommentById(commentId);
                    if (parentComment.isPresent()){
                        commentReplyList.get(i).setReplyUsername(parentComment.get().getUser().getUsername());
                    }
                }
            }
            return replyCommentList.get();
        }
        throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
    }

    /**
     * Deletes a comment whether its a reply or not.
     *
     * @param commentId id of the comment to exist
     */
    @Transactional
    public void deleteComment(Long commentId){
        Optional<Comment> commentopt = commentRepo.findCommentById(commentId);
        if (commentopt.isPresent()) {
            Comment comment = commentopt.get();
            //If its a reply, remove a reply count from the parent
            if (comment.getParentId() != null) {
                Comment parentComment = commentRepo.getById(commentId);
                parentComment.setReplyCount(parentComment.getReplyCount() - 1);
                commentRepo.save(parentComment);
            }

            //Reduce comments number from post and delete comment
            Post post = postRepo.getById(comment.getPostId());
            post.setCommentsNumber(postRepo.getById(comment.getPostId()).getCommentsNumber() - 1);
            postRepo.save(post);
            comment.setDeleted(true);
            comment.setDeletionDate((double) CurrentTime.getCurrentTime());
        }
    }
}
