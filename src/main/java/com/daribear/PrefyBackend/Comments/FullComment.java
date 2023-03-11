package com.daribear.PrefyBackend.Comments;

import com.daribear.PrefyBackend.Users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class FullComment {
    private Long id;
    private Double creationDate;
    private Long postId;
    private String text;
    private Integer replyCount;
    private Long userId;
    private Long parentId;
    private User user;
    private List<Comment> replyList;

    public void convertComment(Comment comment){
        setId(comment.getId());
        setCreationDate(comment.getCreationDate());
        setPostId(comment.getPostId());
        setText(comment.getText());
        setReplyCount(comment.getReplyCount());
        setUserId(comment.getUser().getId());
        setParentId(comment.getParentId());
        setUser(comment.getUser());
        replyList = new ArrayList<>();
    }

}
