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

    @Query("SELECT p FROM Post p WHERE p.id = ?1 AND p.deleted = 0")
    Optional<Post> findPostById(Long id);


    // BOT QUERY
    @Query("SELECT p FROM Post p WHERE p.popular = ?1 AND p.featured = ?2 AND p.deleted = 0")
    Optional<List<Post>> findBotPosts(Boolean popular, Boolean featured, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.featured = true AND p.deleted = 0")
    Optional<List<Post>> findBotOnlyFea(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.popular = true AND p.deleted = 0")
    Optional<List<Post>> findBotOnlyPop(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.userId = ?1 AND p.deleted = 0")
    ArrayList<Post> findPostListById(Long id,Pageable pageable);

    @Query(value = "SELECT p FROM Post p WHERE :category MEMBER OF p.categoryList AND p.deleted = 0")
    ArrayList<Post> findPostListByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.featured = true AND p.deleted = 0")
    ArrayList<Post> findFeaturedPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.popular = true AND p.deleted = 0 AND p.popularDate < :lastPopDate AND NOT EXISTS(Select c FROM CurrentVote c WHERE c.userId = :userId AND c.postId = p.id)")
    ArrayList<Post> findPopularPosts(Pageable pageable, @Param("lastPopDate") Double lastPopularDate, @Param("userId") Long userId);

    @Query("SELECT p FROM Post p WHERE p.popular = true AND p.deleted = 0 AND NOT EXISTS(Select c FROM CurrentVote c WHERE c.userId = :userId AND c.postId = p.id) AND p.id NOT IN :avoidList")
    ArrayList<Post> findNewPopularPosts(Pageable pageable,@Param("userId") Long userId, @Param("avoidList") ArrayList<Long> avoidList);


    @Query("SELECT p FROM Post p WHERE p.deleted = 0")
    ArrayList<Post> findExploreRecentPosts(Pageable pageable);


}
