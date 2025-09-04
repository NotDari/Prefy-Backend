package com.daribear.PrefyBackend.Authentication.Registration;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

/**
 * The data entity which represents the Registration Request.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private final String fullname;
    private final String email;
    private final String password;
    private final String username;

    private final Date DOB;
    private final String token;
}
