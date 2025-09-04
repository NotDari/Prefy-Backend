package com.daribear.PrefyBackend.UserInfo;

import com.daribear.PrefyBackend.Posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The repository for user info, which contains the critical details of the user.
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
