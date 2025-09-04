package com.daribear.PrefyBackend.Users.UserDetails.DetailsReturnClasses;

import lombok.Data;


/**
 * A class to return to the user, to indicate whether the requested username was already taken
 */
@Data
public class UsernameUpdaterReturn {
    private Boolean usernameTaken;
}
