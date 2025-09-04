package com.daribear.PrefyBackend.Users;


import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.IncomeClasses.DefaultIncomePageable;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostIdList;
import com.daribear.PrefyBackend.IncomeClasses.IncomeUserIdList;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for handling the retrieval and creation of users.
 */
@RestController
@RequestMapping(path = "prefy/v1/Users")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private AuthenticationService authService;


    /**
     *
     * @param text text to search usernames for
     * @param pageNumber pageNumber to query
     * @param limit max number of users per page
     * @return List of users to search for
     */
    @GetMapping("/UserSearch/{text}")
    public List<User> searchUsers(@PathVariable String text, @RequestParam Integer pageNumber, Integer limit){
        return userService.initSearch(text, pageNumber, limit);
    }

    /**
     * Gets the list of users ordered by total number of votes(descending).
     * @param pageNumber pageNumber to query
     * @param limit max number of users per page
     * @return List of users on the page
     */
    @GetMapping("/TopUsers")
    public List<User> getTopUsers( @RequestParam Integer pageNumber, Integer limit){
        return userService.getTopUsers(pageNumber, limit);
    }

    /**
     * Creates a new user within the database.
     * @param user user to be added
     */
    @PostMapping("/SubmitUser")
    @PermitAll
    public void createNewUser(@RequestBody User user){
        userService.addNewUser(user);
    }

    /**
     * Attempts to get a user by their username/email (whichever is provided)
     *
     * @param username username/email to be searched for
     * @return user with given details
     */
    @GetMapping("/GetUser")
    public User createNewUser(@RequestParam String username){
        Optional<User> user;
        if (username.contains("@")){
            Optional<Authentication> authentication = authService.getUserByEmail(username);
            if (authentication.isPresent()){
                Long id = authentication.get().getId();
                user = userService.findById(id);
            }else {
                throw new CustomError(HttpServletResponse.SC_BAD_REQUEST, "Failed to find user", 10);
            }
        }else {
            user = userService. findByUsername(username);
        }
        if (user.isPresent()){
            return user.get();
        } else {
            throw new CustomError(HttpServletResponse.SC_BAD_REQUEST, "Failed to find user", 10);
        }
    }


    /**
     * Gets a list of users, who each have one of the provided user ids.
     *
     * @param incomeUserIdList list of user ids
     * @return List of users
     */
    @GetMapping("/GetUserByIdList")
    public ArrayList<User> getUserByIdList(IncomeUserIdList incomeUserIdList){
        return userService.getUserListByIds(incomeUserIdList.getIdList());
    }

    /**
     * Retrieves a user with the provided id(or null).
     * Local validation should rarely make it so that a user is null.
     *
     * @param id id of the user to retrieve
     * @return user with the id
     */
    @GetMapping("/GetUserById")
    public User getUserById(@RequestParam Long id){
        return userService.getUserById(id);
    }


}
