package com.dicoding.sutoriku.data.retrofit

import com.dicoding.sutoriku.data.response.addsutori.AddSutoriResponse
import com.dicoding.sutoriku.data.response.detail.DetailResponse
import com.dicoding.sutoriku.data.response.login.LoginResponse
import com.dicoding.sutoriku.data.response.register.RegisterResponse
import com.dicoding.sutoriku.data.response.sutori.SutoriResponse
import okhttp3.*
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): SutoriResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") id: String
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun addStutori(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): AddSutoriResponse

    @GET("stories")
    suspend fun getSutoriLocation(
        @Query("location") location: Int = 1
    ): SutoriResponse
}