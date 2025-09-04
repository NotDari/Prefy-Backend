package com.daribear.PrefyBackend.Users.UserDetails;

import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserRepository;
import com.daribear.PrefyBackend.Users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This is the service which handles user profile tasks such as editing user image/social media handles/bio etc.
 * This class represents the stuff the user can modify.
 */
@Service
@Component
@AllArgsConstructor
public class UserDetailsEditService {
    private UserRepository userRepo;
    private UserService userService;

    /**
     * Attempts to retrieve a user with the given id.
     * @param id id of the user to get
     * @return User if exists/ null if it doesn't
     */
    private User getUserById(Long id){
        Optional<User> optUser = userRepo.findUserByID(id);
        if (optUser.isEmpty()){
            return null;
        } else {
            return optUser.get();
        }
    }

    /**
     * Updates the user's url image with a new link thats provided.
     *
     * @param userId id of the user to update
     * @param newImageURL new url of the user's image
     */
    public void updateUserImage(Long userId, String newImageURL){
        User user = getUserById(userId);
        user.setProfileImageURL(newImageURL);
        userRepo.save(user);
    }

    /**
     * Attempts to update the user's username in the repository.
     *
     * @param userId id of the user to update
     * @param username new username of the user
     * @return whether the update was successful
     */
    public Boolean updateUsername(Long userId, String username){
        if (userService.userNameExists(username)){
            return false;
        }
        User user = getUserById(userId);
        user.setUsername(username);
        userRepo.save(user);
        return true;
    }

    /**
     * Attempts to update the bio of the user in the repo.
     *
     * @param userId id of the user to update
     * @param bio new bio of the user.
     */
    public void updateBio(Long userId, String bio){
        User user = getUserById(userId);
        user.setBio(bio);
        userRepo.save(user);
    }

    /**
     * Updates the full name of the user.
     *
     * @param userId id of the user to update
     * @param fullName new fullname of the user.
     */
    public void updateFullName(Long userId, String fullName){
        User user = getUserById(userId);
        user.setFullname(fullName);
        userRepo.save(user);
    }

    /**
     * Attempts to update a given social media handle of the user with a new value, throwing an error if it cannot.
     *
     * @param userId id of the user to update
     * @param type which social media handle we're updating
     * @param value new value of the social media handle.
     */
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

