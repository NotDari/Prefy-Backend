package com.daribear.PrefyBackend.Follow;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Follow{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long userId;
    private Long followerId;
    private Double followDate;
}
