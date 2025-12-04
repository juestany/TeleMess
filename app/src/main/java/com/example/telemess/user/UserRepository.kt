package com.example.telemess.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserRepository(private val userDAO: UserDAO) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertUser(newUser: User) {
        coroutineScope.launch(Dispatchers.IO) {
            userDAO.insertUser(newUser)
        }
    }

    fun getUser(name: String, password: String) {
        coroutineScope.launch(Dispatchers.Main) {
            userDAO.getUser(name, password)
        }
    }
}