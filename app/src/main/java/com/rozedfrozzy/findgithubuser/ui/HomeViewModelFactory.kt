package com.rozedfrozzy.findgithubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rozedfrozzy.findgithubuser.data.network.SchedulersWrappers
import java.lang.IllegalArgumentException

class HomeViewModelFactory(val schedulersWrappers: SchedulersWrappers) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(schedulersWrappers) as T
        } else {
            throw IllegalArgumentException()
        }
    }

}