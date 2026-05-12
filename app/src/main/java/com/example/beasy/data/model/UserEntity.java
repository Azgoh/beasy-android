package com.example.beasy.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(
        tableName = "users",
        indices = {
                @Index(value = "email", unique = true),
                @Index(value = "username", unique = true)
        }
)
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String email;
    public String password;
    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email    = email;
        this.password = password;
    }
}