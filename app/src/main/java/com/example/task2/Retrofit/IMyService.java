package com.example.task2.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {

    @POST("uploadC")
    @FormUrlEncoded
    Observable<String> uploadContact(
        @Field("user_id") String user_id,
        @Field("contact_id") String contact_id,
        @Field("name") String name,
        @Field("phone_number") String phone_number,
        @Field("email") String email,
        @Field("photo") String photo);

    @POST("downloadC")
    @FormUrlEncoded
    Observable<String> downloadContact(
        @Field("user_id") String user_id);

    @POST("deleteC")
    @FormUrlEncoded
    Observable<String> deleteContact(
        @Field("user_id") String user_id,
        @Field("contact_id") String contact_id,
        @Field("name") String name,
        @Field("phone_number") String phone_number,
        @Field("email") String email);
}
