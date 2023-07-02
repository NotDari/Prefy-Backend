package com.daribear.PrefyBackend.Posts;


import com.daribear.PrefyBackend.CurrentVote.CurrentVote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    private Double popularDate;
    private Boolean featured;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean deleted;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Double deletionDate;


}
