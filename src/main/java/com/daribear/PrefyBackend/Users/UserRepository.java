package com.daribear.PrefyBackend.Users;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository for finding users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Finds an active user if exists who have the given id
    @Query("SELECT u FROM User u WHERE u.Id = ?1 AND u.deleted = 0")
    Optional<User> findUserByID(Long Id);

    //Searches for a list of active users within the given page, for a given string
    @Query("Select u FROM User u WHERE u.username LIKE %?1% AND u.deleted = 0")
    Optional<List<User>> findUserBySearch(String search,Pageable pageable);

    //Finds a list of the top active users within the given pageable(the pageable should order them)
    @Query("Select u FROM User u WHERE u.deleted = 0")
    Optional<List<User>> findTopUsers(Pageable pageable);

    //Attempt to find a user by username
    @Query("SELECT u from User u WHERE lower(u.username) = lower(:username) AND u.deleted = 0")
    Optional<User> findByUsername(@Param("username") String username);


}
