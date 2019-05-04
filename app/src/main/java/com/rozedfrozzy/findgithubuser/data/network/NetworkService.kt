package com.rozedfrozzy.findgithubuser.data.network

import com.rozedfrozzy.findgithubuser.data.response.SearchResultResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NetworkService {
    private var mRetrofit: Retrofit? = null

    fun fetchAddress(address: String, page: Int): Single<SearchResultResponse>? {
        return getRetrofit()?.create(NetworkService.ApiServices::class.java)?.getGithubUsers(address, page)
    }

    private fun getRetrofit(): Retrofit? {
        if (mRetrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            mRetrofit = Retrofit
                .Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        }
        return mRetrofit
    }

    interface ApiServices {
        @GET("/search/users")
        fun getGithubUsers (
            @Query("q") username: String,
            @Query("page") page: Int = 1,
            @Query("per_page") perPage: Int = 50
        ): Single<SearchResultResponse>
    }
}