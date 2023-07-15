package com.daribear.PrefyBackend.Posts;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.IncomeClasses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "prefy/v1/Posts")
public class PostController {
    private PostService postService;

    public PostController() {
    }

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/AllPosts")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Post> getAllPosts(@AuthenticationPrincipal Authentication authentication){
        return postService.getAllPosts();
    }

    @PostMapping("/SubmitPost")
    public void createNewPost(@RequestBody Post post){
        postService.addNewPost(post);
    }

    @PostMapping("/PostVote")
    public void updatePost(@RequestBody PostVote postVote){
        postService.updatePostVote(postVote);
    }

    @GetMapping("/PostListByIdList")
    public ArrayList<Post> getPostListByIdList(IncomePostIdList incomePostIdList){
        return postService.getPostListByIds(new ArrayList<>(incomePostIdList.getIdList()));
    }

    @GetMapping("/PostById")
    public Post getPostById(IncomePostById incomePostById){
        return postService.getPostById(incomePostById.getId());
    }

    @GetMapping("/PostSearch")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Post> getPostBySearch(IncomePostListBySearch incomePostListBySearch){
        return postService.getPostListBySearch(incomePostListBySearch);
    }


    @GetMapping("/PostListById")
    public ArrayList<Post> getPostListById(IncomePostListById incomePostListById){
        return postService.getPostListById(incomePostListById);
    }
    @GetMapping("/PostListByCategory")
    public ArrayList<Post> getPostListByCategory(IncomePostListByCategory incomePostListByCategory){
        return postService.getPostListByCategory(incomePostListByCategory);
    }
    @GetMapping("/FeaturedPosts")
    public ArrayList<Post> getFeaturedPosts(DefaultIncomePageable defaultIncomePageable){
        return postService.getFeaturedPosts(defaultIncomePageable);
    }




    @PostMapping("/PopularPosts")
    public ArrayList<Post> getNewPopularPosts(@RequestBody NewPopularIncomePageable newPopularIncomePageable){
        return postService.getPopularPosts(newPopularIncomePageable);
    }

    @GetMapping("/ExploreRecentPosts")
    public ArrayList<Post> getExplorePosts(DefaultIncomePageable defaultIncomePageable){
        return postService.getExploreRecentPosts(defaultIncomePageable);
    }

    @PutMapping("/DeletePost")
    public void deletePost(@RequestBody DeleteItemWrapper deleteItemWrapper){
        postService.deletePost(deleteItemWrapper.getItemId(), deleteItemWrapper.getUserId());
    }









}
