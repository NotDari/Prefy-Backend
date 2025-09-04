package com.daribear.PrefyBackend.Users;


import com.daribear.PrefyBackend.Authentication.Registration.RegistrationActivities;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The service class handle user operations such as user creation, retrieval editing and other stuff.
 *
 */
@Service
@Component
@AllArgsConstructor
public class UserService {
    private UserRepository userRepo;
    private RegistrationActivities registrationActivities;


    /**
     * Adds a new user to the database via the user repository, and creates new activites for the user
     * @param user user to be created
     */
    public void addNewUser(User user){
        registrationActivities.initActivities(user);
        userRepo.save(user);
    }

    /**
     * Attempts to retrieve a user by hte username.
     *
     * @param username the user to try and find
     * @return optional containing the user if exists
     */
    public Optional<User> findByUsername(String username){
        return userRepo.findByUsername(username);
    }

    /**
     * Uses a string to search for users with a similar name, returning pages of options.
     * Used in the app for user search.
     *
     * @param text text to search with
     * @param pageNumber pageNumber of search
     * @param limit number of users to limit at on each page
     * @return list of users with a similar or equal username
     */
    public List<User> initSearch(String text, Integer pageNumber, Integer limit){
        Pageable paging = PageRequest.of(pageNumber, limit);
        Optional<List<User>> userList = userRepo.findUserBySearch(text,paging);
        if (userList.isPresent()){
            return userList.get();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Get a list of users ordered by number of total votes on their profile(descending) where total votes is the sum
     * of votes on all their posts.
     *
     * @param pageNumber pageNumber of the top users
     * @param limit max number of users on a page
     * @return list of users who have the most total votes
     */
    public List<User> getTopUsers(Integer pageNumber, Integer limit){
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by("votesNumber").descending());
        Optional<List<User>> userList = userRepo.findTopUsers(pageable);
        return userList.orElseGet(ArrayList::new);
    }

    /**
     * Checks whether a user currently holds the username that is being checked
     * @param username the username to check
     * @return whether the username exists currently with a user
     */
    public Boolean userNameExists(String username){
        return userRepo.findByUsername(username).isPresent();
    }

    /**
     * Checks whether a user exists with the giving id, returning the user if so.
     *
     * @param id id of user to check
     * @return Optional containing the user if it exists
     */
    public Optional<User> findById(Long id){
        return userRepo.findById(id);
    }

    /**
     * Uses a list of ids and returns the associated users.
     *
     * @param idList list of user Ids to find users for
     * @return List of users containing who have ids required
     */
    public ArrayList<User> getUserListByIds(ArrayList<Long> idList){
        ArrayList<User> userList = new ArrayList<>();
        for (Long id : idList){
            userList.add(getUserById(id));
        }
        return userList;
    }

    /**
     * Returns a user(or null) given an id
     * @param id id of the user to retrieve
     * @return the user with the given id(or null if no user)
     */
    public User getUserById(Long id){
        Optional<User> optUser = userRepo.findUserByID(id);
        if (optUser.isEmpty()){
            return null;
        } else {
            return optUser.get();
        }
    }

    /**
     * Either increases or decreases the follower count of a user by 1
     *
     * @param userId id of the user to alter
     * @param increase true for increase, false for decrease
     */
    public void alterFollowersCount(Long userId, Boolean increase){
        Optional<User> optUser = findById(userId);
        Integer value = 1;
        if (!increase){
            value = -1;
        }
        if (optUser.isPresent()){
            User user = optUser.get();
            user.setFollowerNumber(user.getFollowerNumber() + value);
            userRepo.save(user);
        }else {
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.USERNOTFOUND);
        }
    }

    /**
     * Either increases or decreases the following count of a user by 1
     *
     * @param userId id of the user to alter
     * @param increase true for increase, false for decrease
     */
    public void alterFollowingCount(Long userId, Boolean increase){
        Optional<User> optUser = findById(userId);
        Integer value = 1;
        if (!increase){
            value = -1;
        }
        if (optUser.isPresent()){
            User user = optUser.get();
            user.setFollowingNumber(user.getFollowingNumber() + value);
            userRepo.save(user);
        }else {
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.USERNOTFOUND);
        }
    }
}
