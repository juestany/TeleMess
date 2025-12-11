package com.example.telemess.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserRepository(private val userDAO: UserDAO) {
    suspend fun insertUser(newUser: User) {
        userDAO.insertUser(newUser)
    }

    fun getUser(username: String, password: String): User? {
        return userDAO.getUser(username, password)
    }
}