package com.daribear.PrefyBackend.CurrentVote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CurrentVotes")
@NoArgsConstructor
@AllArgsConstructor
public class CurrentVote {
    @SequenceGenerator(
            name = "CurrentVote_Sequence",
            sequenceName = "CurrentVote_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "CurrentVote_Sequence"
    )

    @Id
    private Long voteId;

    public CurrentVote(Long userId, Long postId, String currentVote) {
        this.userId = userId;
        this.postId = postId;
        this.currentVote = currentVote;
    }

    private Long userId;

    private Long postId;
    private String currentVote;

}
