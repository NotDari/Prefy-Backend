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
import com.daribear.PrefyBackend.Utils.CurrentTime;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This service handles all of the interactions between the activityController and the UserActivityRepository,
 * VotesActivityRepository, CommentsActivityRepository and FollowActivityRepository.
 *
 */
@Service
@AllArgsConstructor
public class ActivityService {


    private UserActivityRepository userActivityRepo;
    private VotesActivityRepository votesActivityRepo;
    private CommentsActivityRepository commentsActivityRepo;
    private FollowActivityRepository followActivityRepo;
    private PostRepository postRepo;
    private UserService userService;

    /**
     * Get the 10 most recent commentsActivity for the user for a specific page.
     *
     * @param pageNumber the page to get the data from
     * @param userId the user id of the user to get the data from
     * @return an arraylist of the commentsActivity
     */
    public ArrayList<CommentsActivity> getCommentsActivity(Integer pageNumber, Long userId){
        //Create pageable
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        pageable.getSortOr(sort);

        //Get list of commentsActivity
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

    /**
     * Gets the 10 most recent VotesActivities for the user for a specific page.
     *
     * @param pageNumber pageNumber to retrieve
     * @param userId id of the user to get the activities from
     * @return list of VotesActivity
     */
    public ArrayList<VotesActivity> getVotesActivity(Integer pageNumber, Long userId){
        //Create pageable
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        pageable.getSortOr(sort);

        //Retreive the votesActivity
        Optional<List<VotesActivity>> voteListOpt = votesActivityRepo.findVotesActivityListById(userId, pageable);
        if (voteListOpt.isPresent()){
            ArrayList<VotesActivity> voteList = new ArrayList<>(voteListOpt.get());
            return voteList;
        } else {
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
    }

    /**
     * Gets the 10 most recent FollowActivities for the user for a specific page.
     *
     * @param pageNumber pageNumber to retrieve
     * @param userId id of the user to get the activities from
     * @return list of FollowActivities
     */
    public ArrayList<FollowActivity> getFollowersActivity(Integer pageNumber, Long userId){
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        pageable.getSortOr(sort);
        Optional<List<FollowActivity>> followListOpt = followActivityRepo.findFollowersActivityListById(userId, pageable);
        if (followListOpt.isPresent()){
            ArrayList<FollowActivity> followList = new ArrayList<>(followListOpt.get());
            return followList;
        } else {
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
    }

    /**
     * Attempts to retrieve a UserActivity by the id, throwing an error if one occurs.
     *
     * @param id id of the potential UserActivity
     * @return the UserActivity
     */
    public UserActivity getUserActivity(Long id){
        Optional<UserActivity> optUserActivity = userActivityRepo.findById(id);
        if (optUserActivity.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        return optUserActivity.get();

    }

    /**
     * Updates the UserActivityEntity for a provided user with provided new activity counts.
     * If one the provided count is null, it does not update the count, and uses the one in the database.
     *
     * @param id the user id to update the counts with
     * @param newCommentscount the newCommentsCount to be updated
     * @param newVotescount the newVotescount to be updated
     * @param newFollowsCount the newFollowsCount to be updated
     */
    public void setUserActivity(Long id, Long newCommentscount, Long newVotescount, Long newFollowsCount){
        if (newCommentscount == null || newVotescount == null ){
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

            if (newFollowsCount == null){
                newFollowsCount = tempUserActivity.getNewFollowsCount();
            }

            Long newActivitiesCount = newVotescount + newCommentscount + newFollowsCount;
            userActivityRepo.updateUserActivity(id, newActivitiesCount, newCommentscount, newVotescount, newFollowsCount);
        }
    }

    /**
     * This is called when a user votes on a post. It creates a vote activity and updates the totalActivitiesCount.
     *
     * @param submitterID person who voted
     * @param posterId person who owned the post that was voted on
     * @param postId id of the post
     */
    public void vote(Long submitterID, Long posterId, Long postId){
        Optional<UserActivity> userActivityOpt = userActivityRepo.findById(posterId);
        if (userActivityOpt.isPresent()){
            UserActivity userActivity = userActivityOpt.get();
            userActivity.setNewVotesCount(userActivity.getNewVotesCount() + 1);
            userActivity.setNewActivitiesCount(userActivity.getNewActivitiesCount() + 1);
            VotesActivity votesActivity = new VotesActivity();
            votesActivity.setLastUserId(submitterID);
            votesActivity.setPostId(postId);
            votesActivity.setLastVoteDate(Double.valueOf(CurrentTime.getCurrentTime()));
            votesActivity.setUserId(posterId);
            if (!votesActivityRepo.existsVotesActivityByPostId(postId)) {
                votesActivityRepo.save(votesActivity);
            } else {
                votesActivityRepo.updateVoteActivity(votesActivity.getPostId(), votesActivity.getLastUserId(), votesActivity.getLastVoteDate());
            }
        }
    }

    /**
     * This is called when one user makes a comment on another useers post.
     * This function adds an extra "activity" to the post's owners post to alert them of the new comment.
     * It also adds the commentActivity to the database.
     *
     * @param comment comment to create the activities from
     */
    public void madeComment(Comment comment){
        Optional<UserActivity> userActivityOpt = userActivityRepo.findById(comment.getUser().getId());
        if (userActivityOpt.isPresent()){
            //Add extra activity to the post owners activity
            UserActivity userActivity = userActivityOpt.get();
            userActivity.setNewCommentsCount(userActivity.getNewCommentsCount() + 1);
            userActivity.setNewActivitiesCount(userActivity.getNewActivitiesCount() + 1);

            //Create the commentActivity
            CommentsActivity commentsActivity = new CommentsActivity();
            commentsActivity.setText(comment.getText());
            commentsActivity.setIsReply(comment.getParentId() != null);
            commentsActivity.setPostId(comment.getPostId());
            commentsActivity.setCreationDate((double) CurrentTime.getCurrentTime());
            commentsActivity.setUserId(comment.getUser().getId());
            commentsActivityRepo.save(commentsActivity);
        }
    }

    /**
     * Updates a users follow activity, and their overall activity counts
     *
     * @param userId the person who is following/unfollowing
     * @param followerId the person who is being followed/unfollowing
     * @param followed whether it is a follow or unfollow
     */
    public void alteredFollowing(Long userId, Long followerId, Boolean followed){
        Optional<UserActivity> userActivityOpt = userActivityRepo.findById(userId);
        if (userActivityOpt.isPresent()){
            Optional<FollowActivity> followActivityOpt = followActivityRepo.findIfExists(userId, followerId);
            UserActivity userActivity = userActivityOpt.get();
            //Check if there already is a followedActivity
            if (followActivityOpt.isPresent()){
                if (followActivityOpt.get().getFollowed() != followed){
                    //Update the followActivity and add the total activity counts
                    userActivity.setNewFollowsCount(userActivity.getNewFollowsCount() + 1);
                    userActivity.setNewActivitiesCount(userActivity.getNewActivitiesCount() + 1);
                    FollowActivity followActivity = followActivityOpt.get();
                    followActivity.setFollowed(followed);
                    followActivity.setOccurrenceDate((double) CurrentTime.getCurrentTime());
                }
            } else {
                //Update the total activity counts and create a new follow activity
                FollowActivity followActivity = new FollowActivity();
                userActivity.setNewFollowsCount(userActivity.getNewFollowsCount()+ 1);
                userActivity.setNewActivitiesCount(userActivity.getNewActivitiesCount() + 1);
                followActivity.setFollowerId(followerId);
                followActivity.setUserId(userId);
                followActivity.setOccurrenceDate((double) CurrentTime.getCurrentTime());
                followActivity.setFollowed(followed);
                followActivityRepo.save(followActivity);
            }
        }

    }

}
