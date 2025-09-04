package com.daribear.PrefyBackend.Admin.Script;

import com.daribear.PrefyBackend.Users.User;
import lombok.Data;

/**
 * The data for the sending of a user via the request body, extended, with an added email.
 */
@Data
public class IncomeAdminUserClass extends User {
    private String email;
}
