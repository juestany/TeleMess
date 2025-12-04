package com.example.telemess.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    fun getUser(username: String, password: String): User?
}