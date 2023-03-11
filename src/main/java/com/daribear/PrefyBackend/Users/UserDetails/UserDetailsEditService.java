package com.daribear.PrefyBackend.Users.UserDetails;

import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserRepository;
import com.daribear.PrefyBackend.Users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Component
@AllArgsConstructor
public class UserDetailsEditService {
    private UserRepository userRepo;
    private UserService userService;

    public User getUserById(Long id){
        Optional<User> optUser = userRepo.findUserByID(id);
        if (optUser.isEmpty()){
            return null;
        } else {
            return optUser.get();
        }
    }

    public void updateUserImage(Long userId, String newImageURL){
        User user = getUserById(userId);
        user.setProfileImageURL(newImageURL);
        userRepo.save(user);
    }

    public Boolean updateUsername(Long userId, String username){
        if (userService.userNameExists(username)){
            return false;
        }
        User user = getUserById(userId);
        user.setUsername(username);
        userRepo.save(user);
        return true;
    }

    public void updateBio(Long userId, String bio){
        User user = getUserById(userId);
        user.setBio(bio);
        userRepo.save(user);
    }
    public void updateFullName(Long userId, String bio){
        User user = getUserById(userId);
        user.setFullname(bio);
        userRepo.save(user);
    }

    public void updateSocialMedia(Long userId, String type, String value){
        User user = getUserById(userId);
        switch (type){
            case "Instagram":
                user.setInstagram(value);
                break;
            case "Vk":
                user.setVk(value);
                break;
            case "Twitter":
                user.setTwitter(value);
                break;
            default:
                throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        userRepo.save(user);
    }

}

