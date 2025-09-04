package com.daribear.PrefyBackend.Admin;


import com.daribear.PrefyBackend.Admin.Script.*;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostListBySearch;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Allows the admin app to perform both the admin tasks as well as the admin script tasks.
 * Utilises the adminService and the adminScriptService to perform these tasks.
 */
@RestController
@RequestMapping(path = "prefy/v1/Admin")
public class AdminController {
    @Autowired
    AdminScriptService adminScriptService;
    @Autowired
    AdminService adminService;

    /**
     * Registers a new user.
     * @param incomeAdminUserClass the input class which contains the user details to register one
     */
    @PostMapping("/RegisterUser")
    public void register(@RequestBody IncomeAdminUserClass incomeAdminUserClass){
        User user = new User();
        {
            user.setUsername(incomeAdminUserClass.getUsername());
            user.setProfileImageURL(incomeAdminUserClass.getProfileImageURL());
            user.setFullname(incomeAdminUserClass.getFullname());
            user.setPostsNumber(incomeAdminUserClass.getPostsNumber());
            user.setVotesNumber(incomeAdminUserClass.getVotesNumber());
            user.setPrefsNumber(incomeAdminUserClass.getPrefsNumber());
            user.setFollowerNumber(incomeAdminUserClass.getFollowerNumber());
            user.setFollowingNumber(incomeAdminUserClass.getFollowingNumber());
            user.setBio(incomeAdminUserClass.getBio());
            user.setVk(incomeAdminUserClass.getVk());
            user.setInstagram(incomeAdminUserClass.getInstagram());
            user.setTwitter(incomeAdminUserClass.getTwitter());
            user.setVerified(incomeAdminUserClass.getVerified());
            user.setDeleted(incomeAdminUserClass.getDeleted());
            user.setDeletionDate(incomeAdminUserClass.getDeletionDate());
        }

        adminScriptService.createAuth(incomeAdminUserClass.getEmail(), user);
    }

    /**
     * Adds a new post.
     * @param incomeAdminPostClass the input class which contains the post details to register it
     * @return OutputAdminPostClass containing the post id and uid
     */
    @PostMapping("/AddPost")
    public OutputAdminPostClass addPost(@RequestBody IncomeAdminPostClass incomeAdminPostClass){
        return adminScriptService.getPostId(incomeAdminPostClass);
    }

    /**
     * Calls the adminScriptService to add a comment
     * @param incomeAdminCommentClass the input class which contains the comment details to register it
     */
    @PostMapping("/AddComment")
    public void addComment(@RequestBody IncomeAdminCommentClass incomeAdminCommentClass){
        adminScriptService.registerComment(incomeAdminCommentClass);
    }

    /**
     * Calls the adminscriptService to add a vote to the database
     * @param incomeAdminVoteClass the input class which contains the vote details to register it
     */
    @PostMapping("/AddVote")
    public void addVote(@RequestBody IncomeAdminVoteClass incomeAdminVoteClass){
        adminScriptService.registerVote(incomeAdminVoteClass);
    }

    /**
     * Replaces a post by using one with the exact same id but with new details
     * @param post replacement post
     */
    @PostMapping("/UpdatePost")
    public void updatePost(@RequestBody Post post){
        adminService.updatePost(post);
    }

}
