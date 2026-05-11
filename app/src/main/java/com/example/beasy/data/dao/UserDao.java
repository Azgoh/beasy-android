package com.example.beasy.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.beasy.data.entity.UserEntity;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertUser(UserEntity user);

    /** Login with email + password */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity findByEmailAndPassword(String email, String password);

    /** Login with username + password */
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    UserEntity findByUsernameAndPassword(String username, String password);

    /** Register: check if username taken */
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity findByUsername(String username);

    /** Register: check if email taken */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity findByEmail(String email);

    /** Get a user by id — useful after login to fetch full profile */
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    UserEntity findById(int id);

    /** Get a user by either username or email */
    @Query("SELECT * FROM users WHERE username = :identifier OR email = :identifier LIMIT 1")
    UserEntity findByIdentifier(String identifier);

}