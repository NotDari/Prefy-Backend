package com.daribear.PrefyBackend.Comments;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The entity which represents the comment. This comment could be the original comment or a reply to a comment.
 */
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @SequenceGenerator(
            name = "Comment_Sequence",
            sequenceName = "Comment_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Comment_Sequence"
    )
    @Id
    private Long id;
    private Double creationDate;
    private Long postId;
    private String text;
    private Integer replyCount;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private Long parentId;
    private Long subParentId;

    @Transient
    private String replyUsername;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean deleted;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Double deletionDate;


}



