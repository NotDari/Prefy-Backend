package com.daribear.PrefyBackend.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long Id;
    private String username;
    private String profileImageURL;
    private String fullname;
    private Long postsNumber;
    private Long votesNumber;
    private Long prefsNumber;
    private Long followerNumber;
    private Long followingNumber;
    private String bio;
    private String vk;
    private String instagram;
    private String twitter;
    private Boolean verified;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean deleted;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Double deletionDate;
}
