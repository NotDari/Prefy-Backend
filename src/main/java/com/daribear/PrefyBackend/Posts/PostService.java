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
        post.setDeleted(false);
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
            if (!vote.equalsIgnoreCase("skip") && !vote.equalsIgnoreCase("none")){
                post.setAllVotes(post.getAllVotes() + 1);
                Optional<User> postUserOptional = userRepo.findUserByID(post.getUserId());
                if (postUserOptional.isPresent()){
                    activityService.vote(postVote.getUserId(), post.getUserId(), post.getId());
                    User user = postUserOptional.get();
                    user.setVotesNumber(user.getVotesNumber() + 1);
                    userRepo.save(user);
                }
                Optional<User> submittedUserOptional = userRepo.findUserByID(post.getUserId());
                if (submittedUserOptional.isPresent()){
                    User user = submittedUserOptional.get();
                    user.setPrefsNumber(user.getPrefsNumber() + 1);
                    userRepo.save(user);
                }else {
                    throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.CurrentUserDeleted);
                }
            }



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


    public Post getPostById(Long postId){
        return postRepo.findPostById(postId).get();
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

    private Pageable createPageable(Integer pageNumber, Integer limit){
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by("creationDate").descending());
        return pageable;
    }

    public ArrayList<Post> getExploreRecentPosts(DefaultIncomePageable defaultIncomePageable){
        return postRepo.findExploreRecentPosts(createPageable(defaultIncomePageable.getPageNumber(), defaultIncomePageable.getLimit()));
    }

    public ArrayList<Post> getPopularPosts(NewPopularIncomePageable newPopularIncomePageable){
        Pageable pageable = PageRequest.of(0, newPopularIncomePageable.getLimit(), Sort.by("popularDate").descending());
        return postRepo.findNewPopularPosts(pageable,newPopularIncomePageable.getUserId(), newPopularIncomePageable.getIgnoreList());
    }


    public List<Post> getPostListBySearch(IncomePostListBySearch incomePostListBySearch){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> cr = cb.createQuery(Post.class);
        Root<Post> root = cr.from(Post.class);
        ArrayList<Predicate> predicates = new ArrayList<>();
        if (incomePostListBySearch.getSearch() != null && !incomePostListBySearch.getSearch().isEmpty()) {
            Predicate predicate = cb.like(root.get("question"), ("%" + incomePostListBySearch.getSearch() + "%"));
            predicates.add(predicate);
        }
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
        if (incomePostListBySearch.getPopular() != null){
            Predicate predicate = cb.equal(root.get("popular"), incomePostListBySearch.getPopular());
            predicates.add(predicate);
        }
        if (incomePostListBySearch.getFeatured() != null){
            Predicate predicate = cb.equal(root.get("featured"), incomePostListBySearch.getFeatured());
            predicates.add(predicate);
        }
        if (incomePostListBySearch.getDeleted() != null){
            Predicate predicate = cb.equal(root.get("deleted"), incomePostListBySearch.getDeleted());
            predicates.add(predicate);
        }
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
        if (predicates.size() > 0) {
            Predicate[] predicateList = new Predicate[predicates.size()];
            for (int i = 0; i < predicates.size(); i++){
                predicateList[i] = predicates.get(i);
            }
            cr.select(root).where(predicateList);
        }

        TypedQuery<Post> query = entityManager.createQuery(cr);


        PagedListHolder pagedListHolder = new PagedListHolder(query.getResultList());
        pagedListHolder.setPageSize(incomePostListBySearch.getLimit());
        pagedListHolder.setPage(incomePostListBySearch.getPageNumber());
        Page<Post> page = new PageImpl<>(pagedListHolder.getPageList(), PageRequest.of(incomePostListBySearch.getPageNumber(), incomePostListBySearch.getLimit()), query.getResultList().size());
        return page.get().toList();
    }



    @Transactional
    public void deletePost(Long postId, Long userId){
        Post post = postRepo.findPostById(postId).get();
        if (userId.equals(post.getUserId())) {
            User user = userRepo.findUserByID(post.getUserId()).get();
            user.setPostsNumber(user.getPostsNumber() - 1);
            post.setDeleted(true);
            post.setDeletionDate((double) CurrentTime.getCurrentTime());
        }
    }

}