package com.daribear.PrefyBackend.Admin;


import com.daribear.PrefyBackend.Admin.Script.*;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostListBySearch;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "prefy/v1/Admin")
public class AdminController {
    @Autowired
    AdminScriptService adminScriptService;
    @Autowired
    AdminService adminService;

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

    @PostMapping("/AddPost")
    public OutputAdminPostClass addPost(@RequestBody IncomeAdminPostClass incomeAdminPostClass){
        return adminScriptService.getPostId(incomeAdminPostClass);
    }

    @PostMapping("/AddComment")
    public void addComment(@RequestBody IncomeAdminCommentClass incomeAdminCommentClass){
        adminScriptService.registerComment(incomeAdminCommentClass);
    }

    @PostMapping("/AddVote")
    public void addVote(@RequestBody IncomeAdminVoteClass incomeAdminVoteClass){
        adminScriptService.registerVote(incomeAdminVoteClass);
    }


    @PostMapping("/UpdatePost")
    public void updatePost(@RequestBody Post post){
        adminService.updatePost(post);
    }

}
