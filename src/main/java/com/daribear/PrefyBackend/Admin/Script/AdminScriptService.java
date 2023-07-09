package com.daribear.PrefyBackend.Admin.Script;


import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationRepository;
import com.daribear.PrefyBackend.Comments.Comment;
import com.daribear.PrefyBackend.Comments.CommentRepository;
import com.daribear.PrefyBackend.CurrentVote.CurrentVote;
import com.daribear.PrefyBackend.CurrentVote.CurrentVoteRepository;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostRepository;
import com.daribear.PrefyBackend.Security.PasswordConfig;
import com.daribear.PrefyBackend.UserInfo.UserInfo;
import com.daribear.PrefyBackend.UserInfo.UserInfoService;
import com.daribear.PrefyBackend.Users.UserRepository;
import com.daribear.PrefyBackend.Users.UserService;
import com.daribear.PrefyBackend.Utils.CurrentTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

import static com.daribear.PrefyBackend.Security.ApplicationUserRole.User;

@Service
@Component
public class AdminScriptService {
    private Boolean ENABLED = false;
    @Autowired
    AuthenticationRepository authRepo;
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    private PasswordConfig passwordConfig;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private CurrentVoteRepository currentVoteRepo;



    public void createAuth(String email, com.daribear.PrefyBackend.Users.User user){
        if (!ENABLED){
            return;
        }
        Authentication authentication = new Authentication(email, createPassword(), User.getGrantedAuthorities());
        authentication.setLocked(false);
        authentication.setEnabled(true);

        authRepo.save(authentication);
        {
            user.setId(authentication.getId());
            if (user.getBio() == null){
                user.setBio("");
            }
            if (user.getInstagram() == null){
                user.setInstagram("");
            }
            if (user.getTwitter() == null){
                user.setTwitter("");
            }
            if (user.getVk() == null){
                user.setVk("");
            }
        }
        userService.addNewUser(user);
        UserInfo userInfo = new UserInfo(authentication, ((Long) CurrentTime.getCurrentTime()).doubleValue(), null);
        userInfoService.saveUserInfo(userInfo);

    }

    public OutputAdminPostClass registerPost(IncomeAdminPostClass incomeAdminPostClass){
        if (!ENABLED){
            return null;
        }
        Post post = new Post();
        {
            post.setLeftVotes(incomeAdminPostClass.getLeftVotes());
            post.setRightVotes(incomeAdminPostClass.getRightVotes());
            post.setCategoryList(incomeAdminPostClass.getCategoryList());
            post.setImageURL(incomeAdminPostClass.getImageURL());
            post.setQuestion(incomeAdminPostClass.getQuestion());
            post.setCommentsNumber(incomeAdminPostClass.getCommentsNumber());
            post.setCreationDate(incomeAdminPostClass.getCreationDate());
            post.setAllVotes(incomeAdminPostClass.getAllVotes());
            post.setPopular(incomeAdminPostClass.getPopular());
            post.setPopularDate(incomeAdminPostClass.getPopularDate());
            post.setFeatured(incomeAdminPostClass.getFeatured());
            post.setDeleted(incomeAdminPostClass.getDeleted());
            post.setDeletionDate(incomeAdminPostClass.getDeletionDate());
        }
        Optional<com.daribear.PrefyBackend.Users.User> user = userService.findByUsername(incomeAdminPostClass.username);
        if (user.isEmpty()) {
            return null;
        }
        post.setUserId(user.get().getId());
        postRepo.save(post);
        OutputAdminPostClass outputPost = new OutputAdminPostClass();
        outputPost.setPostId(post.getId());
        outputPost.setUid(incomeAdminPostClass.getPostUid());
        return outputPost;


    }

    public OutputAdminPostClass getPostId(IncomeAdminPostClass incomeAdminPostClass){
        if (ENABLED) {
            Optional<Post> post = postRepo.findPostByFields(incomeAdminPostClass.getQuestion(), incomeAdminPostClass.getCreationDate(), incomeAdminPostClass.getImageURL());
            OutputAdminPostClass outputPost = new OutputAdminPostClass();
            outputPost.setPostId(post.get().getId());
            outputPost.setUid(incomeAdminPostClass.getPostUid());
            return outputPost;
        }
        return null;
    }
    public void registerComment(IncomeAdminCommentClass incomeAdminCommentClass){
        if (ENABLED){
            Comment comment = new Comment();
            {
                comment.setPostId(incomeAdminCommentClass.getPostId());
                comment.setCreationDate(incomeAdminCommentClass.getCreationDate());
                comment.setText(incomeAdminCommentClass.getText());
                comment.setParentId(incomeAdminCommentClass.getParentId());
                comment.setDeletionDate(incomeAdminCommentClass.getDeletionDate());
                comment.setDeleted(incomeAdminCommentClass.getDeleted());
                comment.setReplyUsername(incomeAdminCommentClass.getReplyUsername());
                comment.setSubParentId(incomeAdminCommentClass.getSubParentId());
                comment.setReplyCount(incomeAdminCommentClass.getReplyCount());
            }
            Optional<com.daribear.PrefyBackend.Users.User> user = userService.findByUsername(incomeAdminCommentClass.getUsername());
            if (user.isPresent()){
                comment.setUser(user.get());
                commentRepo.save(comment);
            }

        }
    }

    public void registerVote(IncomeAdminVoteClass incomeAdminVoteClass){
        if (ENABLED){
            CurrentVote currentVote = new CurrentVote();
            {
                currentVote.setCurrentVote(incomeAdminVoteClass.getCurrentVote());
                currentVote.setUserId(incomeAdminVoteClass.getUserId());
                currentVote.setPostId(incomeAdminVoteClass.getPostId());
            }
            Optional<com.daribear.PrefyBackend.Users.User> user = userService.findByUsername(incomeAdminVoteClass.getUsername());
            if (user.isPresent()){
                currentVote.setUserId(user.get().getId());
                currentVoteRepo.save(currentVote);
            }
        }
    }
    private String createPassword(){
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        String encodedPassword = passwordConfig.PasswordEncoder().encode(sb.toString());

        return encodedPassword;
    }



}
