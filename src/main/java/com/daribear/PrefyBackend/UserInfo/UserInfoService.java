package com.daribear.PrefyBackend.UserInfo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@AllArgsConstructor
public class UserInfoService {
    private UserInfoRepository userInfoRepo;

    public void saveUserInfo(UserInfo userInfo){
        userInfoRepo.save(userInfo);
    }
}
