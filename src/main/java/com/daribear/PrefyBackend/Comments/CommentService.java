package com.daribear.PrefyBackend.Comments;


import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostRepository;
import com.daribear.PrefyBackend.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class CommentService {
    private CommentRepository commentRepo;
    private UserRepository userRepo;

    private PostRepository postRepo;

    public CommentService() {
    }

    @Autowired
    public CommentService (CommentRepository commentRepo, UserRepository userRepo, PostRepository postRepo){
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    @Transactional
    public void addNewComment(Comment comment, Long userId){
        if (checkIfValid(comment)) {
            if (userRepo.existsById(userId)){
                comment.setUser(userRepo.getById(userId));
                Post post = postRepo.findPostById(comment.getPostId()).get();
                post.setCommentsNumber(post.getCommentsNumber() + 1);
                postRepo.save(post);
                commentRepo.save(comment);
            }

        }
    }

    private Boolean checkIfValid(Comment comment){
        if (comment.getParentId() == null){
            return true;
        } else {
            Comment parentComment = commentRepo.getById(comment.getParentId());
            if (parentComment.getReplyCount() == null){
                parentComment.setReplyCount(1);
                commentRepo.saveAndFlush(parentComment);
                return true;
            }
            if (parentComment.getReplyCount() >= 500){
                return false;
            } else {
                parentComment.setReplyCount(parentComment.getReplyCount() + 1);
                commentRepo.saveAndFlush(parentComment);
                return true;
            }
        }
    }

    public List<FullComment> getPostComments(Long postId, Integer pageNumber){
        Pageable mainCommentPageable = PageRequest.of(pageNumber, 10);
        Optional<List<Comment>> commentList = commentRepo.findCommentByPostId(postId, mainCommentPageable);
        List<FullComment> fullCommentList = new ArrayList<>();
        if (commentList.isPresent()){
            for (Comment comment : commentList.get()){
                FullComment fullComment = new FullComment();
                fullComment.convertComment(comment);
                if (fullComment.getReplyCount() != null){
                    if (fullComment.getReplyCount() > 0){
                        Pageable replyCommentPageable = PageRequest.of(0, 10);
                        Optional<List<Comment>> replyCommentList =  commentRepo.findCommentByParentId(fullComment.getId(), replyCommentPageable);
                        if (replyCommentList.isPresent()){
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

    public List<Comment> getCommentReplies(Long commentId, Integer pageNumber){
        Pageable replyCommentPageable = PageRequest.of(pageNumber, 10);
        Optional<List<Comment>> replyCommentList =  commentRepo.findCommentByParentId(commentId, replyCommentPageable);
        if (replyCommentList.isPresent()){
            ArrayList<Comment> commentReplyList = new ArrayList<>(replyCommentList.get());
            for (int i =0;i< commentReplyList.size(); i ++){
                if (commentReplyList.get(i).getSubParentId() != null){
                    Optional<Comment> subParentCommentOpt = commentRepo.findCommentById(commentReplyList.get(i).getSubParentId());
                    if (subParentCommentOpt.isPresent()){
                        commentReplyList.get(i).setReplyUsername(subParentCommentOpt.get().getUser().getUsername());
                    }
                } else {
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

    @Transactional
    public void deleteComment(Long commentId){
        Comment comment = commentRepo.getById(commentId);
        if (comment.getParentId() != null){
            Comment parentComment = commentRepo.getById(commentId);
            parentComment.setReplyCount(parentComment.getReplyCount() - 1);
            commentRepo.save(parentComment);
        }


        Post post = postRepo.getById(comment.getPostId());
        post.setCommentsNumber(postRepo.getById(comment.getPostId()).getCommentsNumber() - 1);
        postRepo.save(post);
        commentRepo.deleteById(commentId);
    }
}
