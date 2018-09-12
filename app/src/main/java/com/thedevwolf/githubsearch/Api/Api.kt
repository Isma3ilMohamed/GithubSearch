package com.thedevwolf.githubsearch.Api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.thedevwolf.githubsearch.BuildConfig
import com.thedevwolf.githubsearch.model.RepoSearchResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    companion object {
        val BASE_URL="https://api.github.com/"

        val IN_QUALIFIER = "in:name,description"

        fun create():Api{
            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG){
                val loggingInterceptor= HttpLoggingInterceptor()
                loggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(loggingInterceptor)
            }
            val retrofit: Retrofit =Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .build()

            return retrofit.create(Api::class.java)
        }
    }

    @GET("search/repositories?sort=stars")
    fun searchRepos(@Query("q") query: String,
                    @Query("page") page: Int,
                    @Query("per_page") itemsPerPage: Int): Observable<RepoSearchResponse>
}

