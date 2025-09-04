package com.daribear.PrefyBackend.Activity.UserActivity.Comments;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


/**
 * Entity for dictating a user's CommentsActivity,
 * which is used for displaying to the user, recent activity on their post.
 * Each commentActivity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CommentsActivity")
public class CommentsActivity {
    @Id
    @SequenceGenerator(
            name = "CommentsActivity_Sequence",
            sequenceName = "CommentsActivity_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "CommentsActivity_Sequence"
    )
    private Long id;
    private Long userId;
    private Long postId;
    private Boolean isReply;
    private String text;
    @Transient
    private String postImageURL;
    private Double creationDate;
}
