package com.daribear.PrefyBackend.Posts;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository representing the handling of the post entity.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Tries to find the post with the given id.
     * @param id id of the post to retrieve
     * @return an optional of the post which is attempting to be found
     */
    @Query("SELECT p FROM Post p WHERE p.id = ?1 AND p.deleted = 0")
    Optional<Post> findPostById(Long id);


    /**
     * This is a special query used by the bot, in order to find specifically tagged posts.
     *
     * @param popular search for posts tagged as popular or posts tagged as not popular
     * @param featured search for posts tagged as featured or posts tagged as not featured
     * @param pageable the page number of the posts
     * @return list of posts matching the criteria
     */
    @Query("SELECT p FROM Post p WHERE p.popular = ?1 AND p.featured = ?2 AND p.deleted = 0")
    Optional<List<Post>> findBotPosts(Boolean popular, Boolean featured, Pageable pageable);


    /**
     * A query used by the bot in order to find posts that are tagged as 'featured'
     * @param pageable pagination details such as the page number and items per page
     * @return list of posts matching the criteria
     */
    @Query("SELECT p FROM Post p WHERE p.featured = true AND p.deleted = 0")
    Optional<List<Post>> findBotOnlyFea(Pageable pageable);

    /**
     * A query used by the bot in order to find posts that are tagged as 'popular'
     * @param pageable pagination details such as the page number and items per page
     * @return list of posts matching the criteria
     */
    @Query("SELECT p FROM Post p WHERE p.popular = true AND p.deleted = 0")
    Optional<List<Post>> findBotOnlyPop(Pageable pageable);

    /**
     * Finds a page of posts for a given user.
     * @param id if of the user
     * @param pageable pagination details such as the page number and items per page
     * @return list of posts matching the criteria
     */
    @Query("SELECT p FROM Post p WHERE p.userId = ?1 AND p.deleted = 0")
    ArrayList<Post> findPostListById(Long id,Pageable pageable);

    /**
     * Finds a page of posts for a given category.
     *
     * @param category category to find posts for
     * @param pageable pagination details such as the page number and items per page
     * @return list of posts matching the criteria
     */
    @Query(value = "SELECT p FROM Post p WHERE :category MEMBER OF p.categoryList AND p.deleted = 0")
    ArrayList<Post> findPostListByCategory(@Param("category") String category, Pageable pageable);

    /**
     * Finds a page of featured posts.
     * @param pageable pagination details such as the page number and items per page
     * @return list of posts matching the criteria
     */
    @Query("SELECT p FROM Post p WHERE p.featured = true AND p.deleted = 0")
    ArrayList<Post> findFeaturedPosts(Pageable pageable);

    /**
     * Finds a page of popularPosts since the last populardate.
     * Removes posts that the user has voted on.
     * This is to stop users seeing popular posts that they have already seen.
     *
     *
     * @param pageable pagination details such as the page number and items per page and ordered by date
     * @param lastPopularDate date of the last post they've seen(date the post became popular not when they saw it)
     * @param userId id of the user making the request
     * @return list of posts matching the criteria
     */
    @Query("SELECT p FROM Post p WHERE p.popular = true AND p.deleted = 0 AND p.popularDate < :lastPopDate AND NOT EXISTS(Select c FROM CurrentVote c WHERE c.userId = :userId AND c.postId = p.id)")
    ArrayList<Post> findPopularPosts(Pageable pageable, @Param("lastPopDate") Double lastPopularDate, @Param("userId") Long userId);

    /**
     * This is a way to find new popular posts for the user.
     * It find a page of popular posts that aren't deleted, are active and the user hasn't voted on and isn't in the avoid list.
     *
     * @param pageable pagination details such as the page number and items per page
     * @param userId id of the user making the request
     * @param avoidList list of popular posts to not return in the query
     * @return list of posts matching the criteria
     */
    @Query("SELECT p FROM Post p WHERE p.popular = true AND p.deleted = 0 AND NOT EXISTS(Select c FROM CurrentVote c WHERE c.userId = :userId AND c.postId = p.id) AND p.id NOT IN :avoidList")
    ArrayList<Post> findNewPopularPosts(Pageable pageable,@Param("userId") Long userId, @Param("avoidList") ArrayList<Long> avoidList);

    /**
     * Finds a page of the most recent posts, given the page details in the pageable.
     * @param pageable pagination details such as the page number and items per page
     * @return list of posts matching the criteria
     */
    @Query("SELECT p FROM Post p WHERE p.deleted = 0")
    ArrayList<Post> findExploreRecentPosts(Pageable pageable);

    /**
     * Attempts to find a post by matching it to certain fields: question, creationDate and imageURL.
     *
     * @param question the question of the post to try and match
     * @param creationDate the creation date of the post to try and match
     * @param imageURL the url of the image of the post to try and match
     * @return post matching the criteria
     */
    @Query("SELECT p FROM Post p WHERE p.question = ?1 AND p.creationDate = ?2 AND p.imageURL = ?3 AND p.deleted = 0")
    Optional<Post> findPostByFields(String question, Double creationDate, String imageURL );



}
