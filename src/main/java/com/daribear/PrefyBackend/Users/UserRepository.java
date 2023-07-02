package com.daribear.PrefyBackend.Users;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.Id = ?1 AND u.deleted = 0")
    Optional<User> findUserByID(Long Id);

    @Query("Select u FROM User u WHERE u.username LIKE %?1% AND u.deleted = 0")
    Optional<List<User>> findUserBySearch(String search,Pageable pageable);

    @Query("Select u FROM User u WHERE  u.deleted = 0")
    Optional<List<User>> findTopUsers(Pageable pageable);



    @Query("SELECT u from User u WHERE lower(u.username) = lower(:username) AND u.deleted = 0")
    Optional<User> findByUsername(@Param("username") String username);


}
