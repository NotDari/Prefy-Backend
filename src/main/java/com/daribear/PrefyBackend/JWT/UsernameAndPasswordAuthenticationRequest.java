package com.daribear.PrefyBackend.JWT;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsernameAndPasswordAuthenticationRequest {

    private String username;
    private String password;
}
