package com.example.telemess

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.telemess.user.User
import com.example.telemess.user.UserRepository
import com.example.telemess.user.UserRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val userDb = UserRoomDatabase.getInstance(application)
        val userDAO = userDb.userDAO()
        repository = UserRepository(userDAO)
    }

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

    fun getUser(name: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUser(name, password)
        }
    }
}