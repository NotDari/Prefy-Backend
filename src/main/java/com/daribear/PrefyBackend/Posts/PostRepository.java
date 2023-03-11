package com.daribear.PrefyBackend.Posts;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.id = ?1")
    Optional<Post> findPostById(Long id);


    // BOT QUERY
    @Query("SELECT p FROM Post p WHERE p.popular = ?1 AND p.featured = ?2")
    Optional<List<Post>> findBotPosts(Boolean popular, Boolean featured, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.featured = true")
    Optional<List<Post>> findBotOnlyFea(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.popular = true")
    Optional<List<Post>> findBotOnlyPop(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.userId = ?1")
    ArrayList<Post> findPostListById(Long id,Pageable pageable);

    @Query(value = "SELECT p FROM Post p WHERE :category MEMBER OF p.categoryList")
    ArrayList<Post> findPostListByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.featured = true")
    ArrayList<Post> findFeaturedPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.popular = true and ")
    ArrayList<Post> findPopularPosts(Pageable pageable);

    @Query("SELECT p FROM Post p")
    ArrayList<Post> findExploreRecentPosts(Pageable pageable);


}
