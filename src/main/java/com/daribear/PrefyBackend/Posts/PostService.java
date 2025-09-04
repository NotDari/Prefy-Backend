package com.daribear.PrefyBackend.Posts;

import com.daribear.PrefyBackend.Activity.ActivityService;
import com.daribear.PrefyBackend.CurrentVote.CurrentVote;
import com.daribear.PrefyBackend.CurrentVote.CurrentVoteService;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.IncomeClasses.*;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserRepository;

import com.daribear.PrefyBackend.Utils.CurrentTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The service layer handling everything to do with the post entity.
 */
@Service
@Component
public class PostService {
    private PostRepository postRepo;

    private UserRepository userRepo;
    private CurrentVoteService currentVoteService;
    private ActivityService activityService;

    @Autowired
    private EntityManager entityManager;




    public PostService() {
    }

    //Autowired constructor for all the necessary repositories and other services
    @Autowired
    public PostService(PostRepository postRepo, UserRepository userRepo, ActivityService activityService, CurrentVoteService currentVoteService) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.activityService = activityService;
        this.currentVoteService = currentVoteService;
    }

    /**
     * Dangerous query.
     * Returns every post in the database
     *
     * @return list of every post in the system
     */
    public List<Post> getAllPosts() {
        return postRepo.findAll();

    }

    /**
     * Adds a post to the repository.
     *
     * @param post the post to potentially be added to the repository.
     */
    @Transactional
    public void addNewPost(Post post) {
        User user = userRepo.getById(post.getUserId());
        post.setDeleted(false);
        user.setPostsNumber(user.getPostsNumber() + 1);
        userRepo.save(user);
        postRepo.save(post);
    }

    /**
     * This is called when a user votes on a post.
     * If the post doesn't exist, throws an error.
     * If it does checks if the vote is a skip or left or right vote and adds it to the post stats.
     * Since changing of votes is not allowed, they can only vote if they haven't already.
     *
     * @param postVote the vote that has been added to the post
     */
    public void updatePostVote(PostVote postVote) {
        Long id = postVote.getId();
        String vote = postVote.getVote();
        Long userId = postVote.getUserId();
        Optional<Post> postOptional = postRepo.findPostById(id);
        //Check if post exists
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            //Getting what the vote is
            if (vote.equalsIgnoreCase("Right")) {
                post.setRightVotes(post.getRightVotes() + 1);
            } else if (vote.equalsIgnoreCase("Left")) {
                post.setLeftVotes(post.getLeftVotes() + 1);
            }
            //Checking that vote is an actual vote, not just a skip or none
            if (!vote.equalsIgnoreCase("skip") && !vote.equalsIgnoreCase("none")){
                post.setAllVotes(post.getAllVotes() + 1);
                Optional<User> postUserOptional = userRepo.findUserByID(post.getUserId());
                if (postUserOptional.isPresent()){
                    //Increasing the owner of the post user's vote number and creating the activity
                    activityService.vote(postVote.getUserId(), post.getUserId(), post.getId());
                    User user = postUserOptional.get();
                    user.setVotesNumber(user.getVotesNumber() + 1);
                    userRepo.save(user);
                }

                Optional<User> submittedUserOptional = userRepo.findUserByID(userId);
                if (submittedUserOptional.isPresent()){
                    //Increasing the user who voted's pref count
                    User user = submittedUserOptional.get();
                    user.setPrefsNumber(user.getPrefsNumber() + 1);
                    userRepo.save(user);
                }else {
                    throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.CurrentUserDeleted);
                }
            }


            //Checking if user has voted before
            if (currentVoteService.getCurrentVote(userId, id).isPresent()){
                String tempcurrentVote = currentVoteService.getCurrentVote(userId, id).get().getCurrentVote();
                if (!(tempcurrentVote.equals("right") || tempcurrentVote.equals("left"))) {
                    postRepo.save(post);
                    CurrentVote currentVote = new CurrentVote(userId, id, vote);
                    currentVoteService.updateCurrentVote(currentVote.getVoteId(), vote);
                }
            }else {
                postRepo.save(post);
                CurrentVote currentVote = new CurrentVote(userId, id, vote);
                currentVoteService.saveCurrentVote(currentVote);
            }


        } else {
            throw new IllegalStateException("Post with id: " + id + " does not exist");
        }

    }

    /**
     * Gets the list of posts based on the required bot queries.
     * Queries whether the posts are popular, featured or both
     * @param pageable page details of the page
     * @param popular whether the query needs popular posts
     * @param featured whether the query needs featured posts
     * @param bothFeaPop if they are both popular and featured
     * @return list of posts that match the criteria
     */
    public Optional<List<Post>> getBotPosts(Pageable pageable, Boolean popular, Boolean featured, Boolean bothFeaPop) {
        if (featured || popular) {
            if (bothFeaPop){
                //Only looking for popular or featured
                if (popular){
                    return postRepo.findBotOnlyPop(pageable);
                }
                if (featured){
                    return postRepo.findBotOnlyFea(pageable);
                }
            }
        }
        //Return posts that are both popular or featured or both aren't (like an xor)
        return postRepo.findBotPosts(popular, featured, pageable);
    }

    /**
     * Uses an incoming list of ids, and finds the posts.
     *
     * @param idList list of ids
     * @return list of posts that match the criteria
     */
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


    /**
     * Finds a post with a given id.
     *
     * @param postId id of the post to be found
     * @return the post with the given id
     */
    public Post getPostById(Long postId){
        return postRepo.findPostById(postId).get();
    }

    /**
     * Retrieve a list of posts from a specific user
     *
     * @param incomePostListById user id of the posts
     * @return List of posts that match the criteria
     */
    public ArrayList<Post> getPostListById(IncomePostListById incomePostListById){
        if (incomePostListById.getPageNumber() == null){
            incomePostListById.setPageNumber(0);
        }
        Pageable pageable = createPageable(incomePostListById.getPageNumber(), incomePostListById.getLimit());
        return postRepo.findPostListById(incomePostListById.getId(), pageable);
    }

    /**
     * Saves a list of posts to the database
     *
     * @param postList list of posts to be submitted
     */
    public void savePostList(List<Post> postList) {
        postRepo.saveAllAndFlush(postList);
    }

    /**
     * Retrieves the page of posts that are within a specific category.
     *
     * @param incomePostListByCategory category and pagination details
     * @return list of posts that match the criteria
     */
    public ArrayList<Post> getPostListByCategory(IncomePostListByCategory incomePostListByCategory){
        Pageable pageable = createPageable(incomePostListByCategory.getPageNumber(), incomePostListByCategory.getLimit());
        return postRepo.findPostListByCategory(incomePostListByCategory.getCategory() , pageable);
    }

    /**
     * Retrieves the page of posts that are featured.
     *
     * @param defaultIncomePageable pagintation details like the pageNumber and the number of items per page
     * @return page of posts that match the criteria
     */
    public ArrayList<Post> getFeaturedPosts(DefaultIncomePageable defaultIncomePageable){
        Pageable pageable = createPageable(defaultIncomePageable.getPageNumber(), defaultIncomePageable.getLimit());
        return postRepo.findFeaturedPosts(pageable);
    }

    /**
     * Creates a pageable from a page number and a limit
     * @param pageNumber page number for the pageable
     * @param limit limit for the pageable
     * @return the created pageable
     */
    private Pageable createPageable(Integer pageNumber, Integer limit){
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by("creationDate").descending());
        return pageable;
    }

    /**
     * Get the list of most recent posts with the given page number.
     * @param defaultIncomePageable pagination details
     * @return page of posts matching the criteria
     */
    public ArrayList<Post> getExploreRecentPosts(DefaultIncomePageable defaultIncomePageable){
        return postRepo.findExploreRecentPosts(createPageable(defaultIncomePageable.getPageNumber(), defaultIncomePageable.getLimit()));
    }

    /**
     * Get the list of popular posts with the pagination details.
     * @param newPopularIncomePageable pagination details and user detail and avoid limit
     * @return page of posts matching the criteria
     */
    public ArrayList<Post> getPopularPosts(NewPopularIncomePageable newPopularIncomePageable){
        Pageable pageable = PageRequest.of(0, newPopularIncomePageable.getLimit(), Sort.by("popularDate").descending());
        return postRepo.findNewPopularPosts(pageable,newPopularIncomePageable.getUserId(), newPopularIncomePageable.getIgnoreList());
    }

    /**
     * Allows for the searching of posts that match many criteria.
     * The criteria are: pageNumber, limit, question, orderBy, minVoteCount, maxVoteCount, popular, featured, deleted.
     *
     * @param incomePostListBySearch criteria of posts to match
     * @return list of posts that match the criteria
     */
    public List<Post> getPostListBySearch(IncomePostListBySearch incomePostListBySearch){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> cr = cb.createQuery(Post.class);
        Root<Post> root = cr.from(Post.class);
        ArrayList<Predicate> predicates = new ArrayList<>();
        //Check if questions match
        if (incomePostListBySearch.getSearch() != null && !incomePostListBySearch.getSearch().isEmpty()) {
            Predicate predicate = cb.like(root.get("question"), ("%" + incomePostListBySearch.getSearch() + "%"));
            predicates.add(predicate);
        }
        //Check if  votes match
        if (incomePostListBySearch.getMinVoteCount() != null && incomePostListBySearch.getMaxVoteCount() != null){
            Predicate predicate = cb.between(root.get("allVotes"), incomePostListBySearch.getMinVoteCount(), incomePostListBySearch.getMaxVoteCount());
            predicates.add(predicate);
        } else if (incomePostListBySearch.getMinVoteCount() != null){
            Predicate predicate = cb.greaterThan(root.get("allVotes"), incomePostListBySearch.getMinVoteCount());
            predicates.add(predicate);
        } else if (incomePostListBySearch.getMaxVoteCount() != null){
            Predicate predicate = cb.lessThan(root.get("allVotes"), incomePostListBySearch.getMaxVoteCount());
            predicates.add(predicate);
        }

        //Check if post matches the popular criteria
        if (incomePostListBySearch.getPopular() != null){
            Predicate predicate = cb.equal(root.get("popular"), incomePostListBySearch.getPopular());
            predicates.add(predicate);
        }
        //Check if post matches the featured criteria
        if (incomePostListBySearch.getFeatured() != null){
            Predicate predicate = cb.equal(root.get("featured"), incomePostListBySearch.getFeatured());
            predicates.add(predicate);
        }
        //Check if post matches the deleted criteria
        if (incomePostListBySearch.getDeleted() != null){
            Predicate predicate = cb.equal(root.get("deleted"), incomePostListBySearch.getDeleted());
            predicates.add(predicate);
        }

        //Get the order by of the search
        if (incomePostListBySearch.getOrderBy() != null) {
            switch (incomePostListBySearch.getOrderBy()) {
                case ("DateD"):
                    cr.orderBy(cb.desc(root.get("creationDate")));
                    break;
                case ("DateA"):
                    cr.orderBy(cb.asc(root.get("creationDate")));
                    break;
            }
        }

        //Create the list of predicates
        if (predicates.size() > 0) {
            Predicate[] predicateList = new Predicate[predicates.size()];
            for (int i = 0; i < predicates.size(); i++){
                predicateList[i] = predicates.get(i);
            }
            cr.select(root).where(predicateList);
        }

        TypedQuery<Post> query = entityManager.createQuery(cr);

        //Create the page of predicates
        PagedListHolder pagedListHolder = new PagedListHolder(query.getResultList());
        pagedListHolder.setPageSize(incomePostListBySearch.getLimit());
        pagedListHolder.setPage(incomePostListBySearch.getPageNumber());
        Page<Post> page = new PageImpl<>(pagedListHolder.getPageList(), PageRequest.of(incomePostListBySearch.getPageNumber(), incomePostListBySearch.getLimit()), query.getResultList().size());
        return page.get().toList();
    }


    /**
     * Deletes a post from the repository, and reduces the user's number of posts
     * @param postId post id
     * @param userId user id
     */
    @Transactional
    public void deletePost(Long postId, Long userId){
        Post post = postRepo.findPostById(postId).get();
        if (userId.equals(post.getUserId())) {
            //Reducing user's number of posts
            User user = userRepo.findUserByID(post.getUserId()).get();
            user.setPostsNumber(user.getPostsNumber() - 1);
            post.setDeleted(true);
            post.setDeletionDate((double) CurrentTime.getCurrentTime());
        }
    }

}