package com.myproject.user_service.repository;

import com.myproject.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u WHERE u.userID = :userID")
    User findUserByUserID(@Param("userID") String userID);
    int deleteByUserID(String userID);
}
