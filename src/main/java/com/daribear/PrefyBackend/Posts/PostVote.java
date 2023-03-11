package com.daribear.PrefyBackend.Posts;


import lombok.Data;

@Data
public class PostVote {
    private Long userId;
    private Long id;
    private String vote;
}
