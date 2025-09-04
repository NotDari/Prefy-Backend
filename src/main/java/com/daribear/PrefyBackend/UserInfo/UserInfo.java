package com.daribear.PrefyBackend.UserInfo;

import com.daribear.PrefyBackend.Authentication.Authentication;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


/**
 * Critical details of a user, which can only be created at account creation.
 */
@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(targetEntity = Authentication.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "authentication_id")
    private Authentication authentication;
    private Double accountCreationDate;
    private Double DOB;


    public UserInfo(Authentication authentication, Double accountCreationDate, Double DOB) {
        this.authentication = authentication;
        this.accountCreationDate = accountCreationDate;
        this.DOB = DOB;
    }
}
