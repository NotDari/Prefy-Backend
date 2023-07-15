package com.daribear.PrefyBackend.Suggestions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {
    @Id
    @SequenceGenerator(
            name = "Suggestion_Sequence",
            sequenceName = "Suggestion_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Suggestion_Sequence"
    )
    private long id;
    private String suggestionText;
    private Double creationDate;
    private Long userId;
}
