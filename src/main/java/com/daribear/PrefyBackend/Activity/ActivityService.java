package com.daribear.PrefyBackend.Activity;

import com.daribear.PrefyBackend.Activity.UserActivity.*;
import com.daribear.PrefyBackend.Comments.Comment;
import com.daribear.PrefyBackend.Comments.CommentRepository;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Users.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ActivityService {


    private UserActivityRepository userActivityRepo;
    private VotesActivityRepository votesActivityRepo;
    private CommentsActivityRepository commentsActivityRepo;


    public ArrayList<CommentsActivity> getCommentsActivity(Integer pageNumber, Long userId){
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        pageable.getSortOr(sort);
        Optional<List<CommentsActivity>> commentListOpt = commentsActivityRepo.findCommentsActivityListById(userId, pageable);
        if (commentListOpt.isPresent()){
            ArrayList<CommentsActivity> commentList = new ArrayList<>(commentListOpt.get());
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
            votesActivity.setPostCreationDate(Double.valueOf(System.currentTimeMillis()));
            votesActivity.setUserId(posterId);
            votesActivityRepo.save(votesActivity);
        }
        return false;
    }
}
