package com.daribear.PrefyBackend.Posts;

import com.daribear.PrefyBackend.Activity.ActivityService;
import com.daribear.PrefyBackend.CurrentVote.CurrentVote;
import com.daribear.PrefyBackend.CurrentVote.CurrentVoteService;
import com.daribear.PrefyBackend.IncomeClasses.DefaultIncomePageable;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostListByCategory;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostListById;
import com.daribear.PrefyBackend.IncomeClasses.PopularIncomePageable;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserRepository;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class PostService {
    private PostRepository postRepo;

    private UserRepository userRepo;
    private CurrentVoteService currentVoteService;
    private ActivityService activityService;


    public PostService() {
    }

    @Autowired
    public PostService(PostRepository postRepo, UserRepository userRepo, ActivityService activityService, CurrentVoteService currentVoteService) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.activityService = activityService;
        this.currentVoteService = currentVoteService;
    }

    public List<Post> getAllPosts() {
        return postRepo.findAll();

    }

    @Transactional
    public void addNewPost(Post post) {
        User user = userRepo.getById(post.getUserId());
        user.setPostsNumber(user.getPostsNumber() + 1);
        userRepo.save(user);
        postRepo.save(post);
    }

    public void updatePostVote(PostVote postVote) {
        Long id = postVote.getId();
        String vote = postVote.getVote();
        Long userId = postVote.getUserId();
        Optional<Post> postOptional = postRepo.findPostById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (vote.equalsIgnoreCase("Right")) {
                post.setRightVotes(post.getRightVotes() + 1);
            } else if (vote.equalsIgnoreCase("Left")) {
                post.setLeftVotes(post.getLeftVotes() + 1);
            }
            post.setAllVotes(post.getAllVotes() + 1);
            Optional<User> postUserOptional = userRepo.findUserByID(post.getUserId());
            if (postUserOptional.isPresent()){
                activityService.vote(id, post.getUserId(), post.getId());
                User user = postUserOptional.get();
                user.setVotesNumber(user.getVotesNumber() + 1);
                userRepo.save(user);
            }
            if (!currentVoteService.getCurrentVote(userId, id).isPresent()) {
                postRepo.save(post);
                CurrentVote currentVote = new CurrentVote(userId, id, vote);
                currentVoteService.saveCurrentVote(currentVote);
            }

        } else {
            throw new IllegalStateException("Post with id: " + id + " does not exist");
        }

    }

    public Optional<List<Post>> getBotPosts(Pageable pageable, Boolean popular, Boolean featured, Boolean bothFeaPop) {
        if (featured || popular) {
            if (bothFeaPop){
                if (popular){
                    return postRepo.findBotOnlyPop(pageable);
                }
                if (featured){
                    return postRepo.findBotOnlyFea(pageable);
                }
            }
        }
        return postRepo.findBotPosts(popular, featured, pageable);
    }

    public ArrayList<Post> getPostListByIds(ArrayList<Long> idList){
        ArrayList<Post> postList = new ArrayList<>();
        for (Long id : idList){
            Optional<Post> optPost = postRepo.findPostById(id);
            if (optPost.isEmpty()){
                postList.add(null);
            } else {
                postList.add(optPost.get());
            }
        }
        return postList;
    }

    public ArrayList<Post> getPostListById(IncomePostListById incomePostListById){
        if (incomePostListById.getPageNumber() == null){
            incomePostListById.setPageNumber(0);
        }
        Pageable pageable = createPageable(incomePostListById.getPageNumber(), incomePostListById.getLimit());
        return postRepo.findPostListById(incomePostListById.getId(), pageable);
    }

    public void savePostList(List<Post> postList) {
        postRepo.saveAllAndFlush(postList);
    }

    public ArrayList<Post> getPostListByCategory(IncomePostListByCategory incomePostListByCategory){
        Pageable pageable = createPageable(incomePostListByCategory.getPageNumber(), incomePostListByCategory.getLimit());
        return postRepo.findPostListByCategory(incomePostListByCategory.getCategory() , pageable);
    }

    public ArrayList<Post> getFeaturedPosts(DefaultIncomePageable defaultIncomePageable){
        Pageable pageable = createPageable(defaultIncomePageable.getPageNumber(), defaultIncomePageable.getLimit());
        return postRepo.findFeaturedPosts(pageable);
    }
    public ArrayList<Post> getPopularPosts(PopularIncomePageable popularIncomePageable){
        Pageable pageable = PageRequest.of(0, popularIncomePageable.getLimit(), Sort.by("popularDate").descending());
        return postRepo.findPopularPosts(pageable, popularIncomePageable.getLastPopularDate(), popularIncomePageable.getUserId());
    }

    private Pageable createPageable(Integer pageNumber, Integer limit){
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by("creationDate").descending());
        return pageable;
    }

    public ArrayList<Post> getExploreRecentPosts(DefaultIncomePageable defaultIncomePageable){
        return postRepo.findExploreRecentPosts(createPageable(defaultIncomePageable.getPageNumber(), defaultIncomePageable.getLimit()));
    }


    @Transactional
    public void deletePost(Long postId){
        Post post = postRepo.findPostById(postId).get();
        User user = userRepo.findUserByID(post.getUserId()).get();
        user.setPostsNumber(user.getPostsNumber() - 1);
        postRepo.delete(post);
    }

}