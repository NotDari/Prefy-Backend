package com.daribear.PrefyBackend.Admin;


import com.daribear.PrefyBackend.IncomeClasses.IncomePostListBySearch;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * The service related to the admin functions.
 */
@Service
@Component
public class AdminService {
    @Autowired
    private PostRepository postRepo;


    /**
     * Replaces the current post with a set id with a new one with the exact same id.
     * @param post the post to replace the current post with the same id
     */
    public void updatePost(Post post){
        postRepo.save(post);
    }
}
