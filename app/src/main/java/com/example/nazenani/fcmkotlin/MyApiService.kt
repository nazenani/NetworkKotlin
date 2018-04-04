package com.example.nazenani.fcmkotlin

import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


data class Result (val access_token: String)

interface MyApiService {

    companion object Factory {
        const val BASE_URL = "https://example.com";
        const val GET_RESOURCE = "/api/auth/login";
        const val GET_LIST_RESOURCE = "/api/members/{id}";
        const val POST_RESOURCE = "/api/auth/reminder";
        const val POST_FORM_RESOURCE = "/api/member/";
        const val PUT_RESOURCE = "/api/member/photo";

        fun create(): MyApiService {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            // Interfaceから実装を取得
            return retrofit.create(MyApiService::class.java);
        }
    }


    @GET(GET_RESOURCE)
    fun get(
            @Query("email") email: String,
            @Query("password") password: String
    ): Observable<Result>


    @GET(GET_LIST_RESOURCE)
    fun getList(
            @Path("id") user_id : Int,
            @Query("email") email: String
    ): Call<List<Result>>


    @POST(POST_RESOURCE)
    fun post(
            @Body objects: Objects
    ): Observable<Result>


    @FormUrlEncoded
    @POST(POST_FORM_RESOURCE)
    fun formPost(
            @Field("member_name") member_name: String
    ): Observable<Result>


    @Multipart
    @PUT(PUT_RESOURCE)
    fun put(
            @Part("photo") photo: RequestBody
    ): Observable<Result>



// https://qiita.com/SYABU555/items/3b280a8e81d2cc897383



}
