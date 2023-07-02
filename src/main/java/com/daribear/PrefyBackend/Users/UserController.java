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

@RestController
@RequestMapping(path = "prefy/v1/Users")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private AuthenticationService authService;



    @GetMapping("/UserSearch/{text}")
    public List<User> getAllUsers(@PathVariable String text, @RequestParam Integer pageNumber, Integer limit){
        return userService.initSearch(text, pageNumber, limit);
    }

    @GetMapping("/TopUsers")
    public List<User> getTopUsers( @RequestParam Integer pageNumber, Integer limit){
        return userService.getTopUsers(pageNumber, limit);
    }

    @PostMapping("/SubmitUser")
    @PermitAll
    public void createNewUser(@RequestBody User user){
        userService.addNewUser(user);
    }

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

    @GetMapping("/GetUserByIdList")
    public ArrayList<User> getUserByIdList(IncomeUserIdList incomeUserIdList){
        return userService.getUserListByIds(incomeUserIdList.getIdList());
    }

    @GetMapping("/GetUserById")
    public User getUserById(@RequestParam Long id){
        return userService.getUserById(id);
    }


}
