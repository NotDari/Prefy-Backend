package com.daribear.PrefyBackend.Activity.UserActivity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Activity")
public class UserActivity {
    @Id
    private Long id;
    private Long newActivitiesCount;
    private Long newCommentsCount;
    private Long newVotesCount;
    private Long newFollowsCount;

}
