package com.daribear.PrefyBackend.Activity;

import com.daribear.PrefyBackend.Activity.UserActivity.*;
import com.daribear.PrefyBackend.Activity.UserActivity.Comments.CommentsActivity;
import com.daribear.PrefyBackend.Activity.UserActivity.Comments.CommentsActivityRepository;
import com.daribear.PrefyBackend.Activity.UserActivity.Follows.FollowActivity;
import com.daribear.PrefyBackend.Activity.UserActivity.Follows.FollowActivityRepository;
import com.daribear.PrefyBackend.Activity.UserActivity.Votes.VotesActivity;
import com.daribear.PrefyBackend.Activity.UserActivity.Votes.VotesActivityRepository;
import com.daribear.PrefyBackend.Comments.Comment;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Follow.Follow;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostRepository;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ActivityService {


    private UserActivityRepository userActivityRepo;
    private VotesActivityRepository votesActivityRepo;
    private CommentsActivityRepository commentsActivityRepo;
    private FollowActivityRepository followActivityRepo;
    private PostRepository postRepo;
    private UserService userService;


    public ArrayList<CommentsActivity> getCommentsActivity(Integer pageNumber, Long userId){
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        pageable.getSortOr(sort);
        Optional<List<CommentsActivity>> commentListOpt = commentsActivityRepo.findCommentsActivityListById(userId, pageable);
        if (commentListOpt.isPresent()){
            ArrayList<CommentsActivity> commentList = new ArrayList<>(commentListOpt.get());
            for (int i =0; i < commentList.size(); i++){
                Post post = postRepo.getById(commentList.get(i).getPostId());
                commentList.get(i).setPostImageURL(post.getImageURL());
            }
            return commentList;
        } else {
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
    }
    public ArrayList<VotesActivity> getVotesActivity(Integer pageNumber, Long userId){
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        pageable.getSortOr(sort);
        Optional<List<VotesActivity>> voteListOpt = votesActivityRepo.findVotesActivityListById(userId, pageable);
        if (voteListOpt.isPresent()){
            ArrayList<VotesActivity> voteList = new ArrayList<>(voteListOpt.get());
            return voteList;
        } else {
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
    }

    public UserActivity getUserActivity(Long id){
        Optional<UserActivity> optUserActivity = userActivityRepo.findById(id);
        if (optUserActivity.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        return optUserActivity.get();

    }


    public void setUserActivity(Long id, Long newCommentscount, Long newVotescount){
        if (newCommentscount == null || newVotescount == null){
            UserActivity tempUserActivity = userActivityRepo.getById(id);
            if (tempUserActivity == null){
                UserActivity userActivity = new UserActivity();
                userActivity.setId(id);
                userActivity.setNewActivitiesCount(0L);
                userActivity.setNewVotesCount(0L);
                userActivity.setNewCommentsCount(0L);
                userActivityRepo.save(userActivity);
                return;
            }
            if (newCommentscount == null){
                newCommentscount = tempUserActivity.getNewCommentsCount();
            }
            if (newVotescount == null){
                newVotescount = tempUserActivity.getNewVotesCount();
            }
            Long newActivitiesCount = newVotescount + newCommentscount;
            userActivityRepo.updateUserActivity(id, newActivitiesCount, newCommentscount, newVotescount);
        }
    }


    public Boolean vote(Long submitterID, Long posterId, Long postId){
        Optional<UserActivity> userActivityOpt = userActivityRepo.findById(posterId);
        if (userActivityOpt.isPresent()){
            UserActivity userActivity = userActivityOpt.get();
            userActivity.setNewVotesCount(userActivity.getNewVotesCount() + 1);
            userActivity.setNewActivitiesCount(userActivity.getNewActivitiesCount() + 1);
            VotesActivity votesActivity = new VotesActivity();
            votesActivity.setLastUserId(submitterID);
            votesActivity.setPostId(postId);
            votesActivity.setLastVoteDate(Double.valueOf(System.currentTimeMillis()));
            votesActivity.setUserId(posterId);
            if (!votesActivityRepo.existsVotesActivityByPostId(postId)) {
                votesActivityRepo.save(votesActivity);
            } else {
                votesActivityRepo.updateVoteActivity(votesActivity.getPostId(), votesActivity.getLastUserId(), votesActivity.getLastVoteDate());
            }
        }
        return false;
    }

    public void madeComment(Comment comment){
        Optional<UserActivity> userActivityOpt = userActivityRepo.findById(comment.getUser().getId());
        if (userActivityOpt.isPresent()){
            UserActivity userActivity = userActivityOpt.get();
            userActivity.setNewCommentsCount(userActivity.getNewCommentsCount() + 1);
            userActivity.setNewActivitiesCount(userActivity.getNewActivitiesCount() + 1);
            CommentsActivity commentsActivity = new CommentsActivity();
            commentsActivity.setText(comment.getText());
            commentsActivity.setIsReply(comment.getParentId() != null);
            commentsActivity.setPostId(comment.getPostId());
            commentsActivity.setCreationDate((double) System.currentTimeMillis());
            commentsActivity.setUserId(comment.getUser().getId());
            commentsActivityRepo.save(commentsActivity);
        }
    }

    public void alteredFollowing(Long userId, Long followerId, Boolean followed){
        Optional<UserActivity> userActivityOpt = userActivityRepo.findById(userId);
        if (userActivityOpt.isPresent()){
            Optional<FollowActivity> followActivityOpt = followActivityRepo.findIfExists(userId, followerId);
            UserActivity userActivity = userActivityOpt.get();
            if (followActivityOpt.isPresent()){
                if (followActivityOpt.get().getFollowed() != followed){
                    userActivity.setNewFollowsCount(userActivity.getNewCommentsCount() + 1);
                    userActivity.setNewActivitiesCount(userActivity.getNewActivitiesCount() + 1);
                    FollowActivity followActivity = followActivityOpt.get();
                    followActivity.setFollowed(followed);
                    followActivity.setOccurrenceDate((double) System.currentTimeMillis());
                }
            } else {
                FollowActivity followActivity = new FollowActivity();
                userActivity.setNewFollowsCount(userActivity.getNewCommentsCount() + 1);
                userActivity.setNewActivitiesCount(userActivity.getNewActivitiesCount() + 1);
                followActivity.setFollowerId(followerId);
                followActivity.setUserId(userId);
                followActivity.setOccurrenceDate((double) System.currentTimeMillis());
                followActivity.setFollowed(followed);
            }
        }

    }

}
