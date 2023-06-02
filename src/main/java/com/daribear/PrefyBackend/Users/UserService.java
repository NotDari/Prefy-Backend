package com.daribear.PrefyBackend.Users;


import com.daribear.PrefyBackend.Authentication.Registration.RegistrationActivities;
import com.daribear.PrefyBackend.Authentication.Registration.RegistrationService;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Posts.Post;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component
@AllArgsConstructor
public class UserService {
    private UserRepository userRepo;
    private RegistrationActivities registrationActivities;



    public void addNewUser(User user){
        registrationActivities.initActivities(user);
        userRepo.save(user);
    }

    public Optional<User> findByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public List<User> initSearch(String text, Integer pageNumber, Integer limit){
        Pageable paging = PageRequest.of(pageNumber, limit);
        Optional<List<User>> userList = userRepo.findUserBySearch(text,paging);
        if (userList.isPresent()){
            return userList.get();
        } else {
            return new ArrayList<>();
        }
    }

    public List<User> getTopUsers(Integer pageNumber, Integer limit){
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by("votesNumber").descending());
        Optional<List<User>> userList = userRepo.findTopUsers(pageable);
        return userList.orElseGet(ArrayList::new);
    }

    public Boolean userNameExists(String username){
        return userRepo.findByUsername(username).isPresent();
    }

    public Optional<User> findById(Long id){
        return userRepo.findById(id);
    }

    public ArrayList<User> getUserListByIds(ArrayList<Long> idList){
        ArrayList<User> userList = new ArrayList<>();
        for (Long id : idList){
            userList.add(getUserById(id));
        }
        return userList;
    }

    public User getUserById(Long id){
        Optional<User> optUser = userRepo.findUserByID(id);

        if (optUser.isEmpty()){
            return null;
        } else {
            return optUser.get();
        }
    }

    public void increaseFollowers(Long userId, Boolean increase){
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

    public void increaseFollowing(Long userId, Boolean increase){
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
