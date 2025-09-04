package com.daribear.PrefyBackend.Comments;


import lombok.Data;

/**
 * This is a data entity which is used to receive a comment submission rewuest from the client.
 * It contains all the details and thus a function to create a comment.
 */
@Data
public class SubmitCommentRequest {
    private Double creationDate;
    private Long postId;
    private String text;
    private Integer replyCount;
    private Long parentId;
    private Long subParentId;
    private Long userId;

    /**
     * Creates a comment from the current instance of SubmitCommentRequest
     * @return the created Comment
     */
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
