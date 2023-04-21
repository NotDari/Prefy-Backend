package com.daribear.PrefyBackend.Activity.UserActivity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VotesActivity")
public class VotesActivity {
    @Id
    @SequenceGenerator(
            name = "VotesActivity_Sequence",
            sequenceName = "VotesActivity_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "VotesActivity_Sequence"
    )
    private Long id;
    private Long userId;
    private Long postId;
    private Long lastUserId;
    private Double lastVoteDate;
}
