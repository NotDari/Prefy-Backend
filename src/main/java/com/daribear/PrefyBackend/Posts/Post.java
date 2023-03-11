package com.daribear.PrefyBackend.Posts;


import com.daribear.PrefyBackend.CurrentVote.CurrentVote;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @SequenceGenerator(
            name = "Post_Sequence",
            sequenceName = "Post_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Post_Sequence"
    )
    private long id;
    private Long userId;
    private Integer leftVotes, rightVotes;

    @ElementCollection
    @CollectionTable(name="post_categories_list",
            joinColumns=@JoinColumn(name="POST_ID"))
    private List<String> categoryList;


    private String imageURL;
    private String question;
    private Integer commentsNumber;
    private Double creationDate;
    private Integer allVotes;
    private Boolean popular;
    private Boolean featured;


}
