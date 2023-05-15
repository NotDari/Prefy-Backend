package com.daribear.PrefyBackend.Activity.UserActivity.Follows;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "FollowActivity")
public class FollowActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long userId;
    private Long followerId;
    private Double occurrenceDate;
    private Boolean followed;
}
