package com.daribear.PrefyBackend.Comments;


import lombok.Data;

@Data
public class SubmitCommentRequest {
    private Double creationDate;
    private Long postId;
    private String text;
    private Integer replyCount;
    private Long parentId;
    private Long subParentId;
    private Long userId;

    public Comment getComment(){
        Comment comment = new Comment();
        comment.setText(text);
        comment.setReplyCount(replyCount);
        comment.setParentId(parentId);
        comment.setCreationDate(creationDate);
        comment.setPostId(postId);
        comment.setSubParentId(subParentId);
        return comment;
    }
}
