package com.daribear.PrefyBackend.Users;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.Id = ?1")
    Optional<User> findUserByID(Long Id);

    @Query("Select u FROM User u WHERE u.username LIKE %?1%")
    Optional<List<User>> findUserBySearch(String search,Pageable pageable);

    @Query("Select u FROM User u")
    Optional<List<User>> findTopUsers(Pageable pageable);

    Optional<User> findByUsernameIgnoreCase(String username);


}
