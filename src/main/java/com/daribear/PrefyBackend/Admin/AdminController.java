package com.daribear.PrefyBackend.Admin;


import com.daribear.PrefyBackend.Authentication.Registration.RegistrationRequest;
import com.daribear.PrefyBackend.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;

@RestController
@RequestMapping(path = "prefy/v1/Admin")
public class AdminController {
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

        adminService.createAuth(incomeAdminUserClass.getEmail(), user);
    }

    @PostMapping("/AddPost")
    public OutputAdminPostClass addPost(@RequestBody IncomeAdminPostClass incomeAdminPostClass){
        return adminService.getPostId(incomeAdminPostClass);
    }

    @PostMapping("/AddComment")
    public void addComment(@RequestBody IncomeAdminCommentClass incomeAdminCommentClass){
        adminService.registerComment(incomeAdminCommentClass);
    }
    @PostMapping("/AddVote")
    public void addVote(@RequestBody IncomeAdminVoteClass incomeAdminVoteClass){
        adminService.registerVote(incomeAdminVoteClass);
    }
}
