package com.daribear.PrefyBackend.Posts;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.IncomeClasses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The controller for the endpoints of creating/retrieving/deleting or anything else related to posts.
 */
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

    /**
     * Makes sure the user is an admin to use this function.
     * Retrieves a list of every post.
     * Use with caution, with a large number of posts, could be catastrophic
     *
     * @param authentication springboot authentication details
     * @return list of every post in the database.
     */
    @GetMapping("/AllPosts")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Post> getAllPosts(@AuthenticationPrincipal Authentication authentication){
        return postService.getAllPosts();
    }

    /**
     * adds a new post to the database.
     * @param post the post to be added.
     */
    @PostMapping("/SubmitPost")
    public void createNewPost(@RequestBody Post post){
        postService.addNewPost(post);
    }

    /**
     * updates a post with a new vote.
     * @param postVote vote that occurred.
     */
    @PostMapping("/PostVote")
    public void updatePost(@RequestBody PostVote postVote){
        postService.updatePostVote(postVote);
    }

    /**
     * Retrieves a list of posts with given ids.
     *
     *
     * @param incomePostIdList list of ids to retrieve the associated posts
     * @return list of posts with the given ids
     */
    @GetMapping("/PostListByIdList")
    public ArrayList<Post> getPostListByIdList(IncomePostIdList incomePostIdList){
        return postService.getPostListByIds(new ArrayList<>(incomePostIdList.getIdList()));
    }

    /**
     * Retrieve a post with the given id
     * @param incomePostById class containing id of post to retrieve.
     * @return the post requested
     */
    @GetMapping("/PostById")
    public Post getPostById(IncomePostById incomePostById){
        return postService.getPostById(incomePostById.getId());
    }

    /**
     * Requires the admin role.
     * Searches for posts and returns a list of posts potentially matching the criteria.
     *
     * @param incomePostListBySearch class containing details required to search
     * @return list of posts matching the search
     */
    @GetMapping("/PostSearch")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Post> getPostBySearch(IncomePostListBySearch incomePostListBySearch){
        return postService.getPostListBySearch(incomePostListBySearch);
    }

    /**
     *
     * @param incomePostListById
     * @return
     */
    @GetMapping("/PostListById")
    public ArrayList<Post> getPostListById(IncomePostListById incomePostListById){
        return postService.getPostListById(incomePostListById);
    }

    /**
     * Retrieve a list of posts within the given category on the given page.
     *
     * @param incomePostListByCategory the category and page details
     * @return List of Posts on the given page within the given category
     */
    @GetMapping("/PostListByCategory")
    public ArrayList<Post> getPostListByCategory(IncomePostListByCategory incomePostListByCategory){
        return postService.getPostListByCategory(incomePostListByCategory);
    }

    /**
     * Retrieves a list of featured posts, for the given page.
     *
     * @param defaultIncomePageable contains solely the page details
     * @return List of Posts that are featured
     */
    @GetMapping("/FeaturedPosts")
    public ArrayList<Post> getFeaturedPosts(DefaultIncomePageable defaultIncomePageable){
        return postService.getFeaturedPosts(defaultIncomePageable);
    }


    /**
     * Returns a list of popular posts on the given page.
     *
     * @param newPopularIncomePageable list of popular posts and the user's id
     * @return the list of posts
     */
    @PostMapping("/PopularPosts")
    public ArrayList<Post> getNewPopularPosts(@RequestBody NewPopularIncomePageable newPopularIncomePageable){
        return postService.getPopularPosts(newPopularIncomePageable);
    }

    /**
     * Returns a list of the most recently posted posts in any category by anyone.
     * @param defaultIncomePageable the page details
     * @return list of recent posts
     */
    @GetMapping("/ExploreRecentPosts")
    public ArrayList<Post> getExplorePosts(DefaultIncomePageable defaultIncomePageable){
        return postService.getExploreRecentPosts(defaultIncomePageable);
    }

    /**
     * Deletes a post.
     * @param deleteItemWrapper contains the post and user ids
     */
    @PutMapping("/DeletePost")
    public void deletePost(@RequestBody DeleteItemWrapper deleteItemWrapper){
        postService.deletePost(deleteItemWrapper.getItemId(), deleteItemWrapper.getUserId());
    }









}
