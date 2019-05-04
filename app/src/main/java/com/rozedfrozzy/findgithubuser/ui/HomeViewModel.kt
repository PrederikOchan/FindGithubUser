package com.rozedfrozzy.findgithubuser.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rozedfrozzy.findgithubuser.data.network.NetworkService
import com.rozedfrozzy.findgithubuser.data.network.SchedulersWrappers
import com.rozedfrozzy.findgithubuser.data.response.Item
import com.rozedfrozzy.findgithubuser.data.response.SearchResultResponse
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException

class HomeViewModel(val schedulersWrappers: SchedulersWrappers) : ViewModel() {
    val resultListObservable = MutableLiveData<List<Item>>()
    val resultListErrorObservable = MutableLiveData<HttpException>()

    fun getResultListObservable(): LiveData<List<Item>> = resultListObservable
    fun getResultListErrorObservable(): LiveData<HttpException> = resultListErrorObservable

    lateinit var networkService: NetworkService

    var currentPage = 1
    var searchKey: String = ""

    @SuppressLint("CheckResult")
    fun searchAddress(address: String, page: Int = 1) {
        networkService.fetchAddress(address, page)!!
            .subscribeOn(schedulersWrappers.io())
            .observeOn(schedulersWrappers.main())
            .subscribeWith(object: DisposableSingleObserver<SearchResultResponse?>() {
                override fun onSuccess(t: SearchResultResponse) {
                    resultListObservable.postValue(fetchItemFromResult(t))
                    currentPage++
                }

                override fun onError(e: Throwable) {
                    resultListErrorObservable.postValue(e as HttpException)
                }
            })
    }

    private fun fetchItemFromResult(it: SearchResultResponse): ArrayList<Item> {
        val list = ArrayList<Item>()
        list.addAll(it.items)
        return list
    }

}