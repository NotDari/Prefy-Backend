package com.daribear.PrefyBackend.Report;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
public class Report {
    @SequenceGenerator(
            name = "Report_Sequence",
            sequenceName = "Report_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Report_Sequence"
    )
    @Id
    private Long id;
    private String type;
    private Long postId;
    private Long userId;
    private Long commentId;
    private String repCategory;
    private Double creationDate;
    private Boolean active;
}
