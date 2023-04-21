package com.daribear.PrefyBackend.Activity.UserActivity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
