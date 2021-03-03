package com.example.task2.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://randomuser.me/api/?results=5
interface PeopleApiService {
    @GET("api")
    fun getData(
        @Query("results")limit:Number
    ): Deferred<PersonData>

    companion object    {
        operator fun invoke():PeopleApiService{
            val requestInterceptor= Interceptor{
                    chain ->
                val url =chain.request()
                    .url()
                    .newBuilder()
                    .build()
                val request=chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHTTPClient= OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)  
                .build()
            return Retrofit.Builder()
                .client(okHTTPClient)
                .baseUrl("https://randomuser.me")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PeopleApiService::class.java)
        }
    }
}
