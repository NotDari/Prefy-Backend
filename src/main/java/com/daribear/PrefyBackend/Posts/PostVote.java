package com.daribear.PrefyBackend.Posts;


import lombok.Data;

/**
 * An entity representing a user's vote on a post
 */
@Data
public class PostVote {
    private Long userId;
    private Long id;
    private String vote;
}
