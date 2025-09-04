package com.daribear.PrefyBackend.UserInfo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * The service for handling the user info of a user. Since the content is critical, and can only be added at account creation,
 * the available methods are limited.
 */
@Service
@Component
@AllArgsConstructor
public class UserInfoService {
    private UserInfoRepository userInfoRepo;

    public void saveUserInfo(UserInfo userInfo){
        userInfoRepo.save(userInfo);
    }
}
