package com.example.task2.Retrofit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    
    @Multipart
    @POST("/gallery/upload")
    Call<Response> uploadImage(
        @Part MultipartBody.Part image);
    
    @POST("uploadG")
    @FormUrlEncoded
    Observable<String> uploadPictureInfo(
        @Field("user_id") String user_id,
        @Field("file_name") String file_name);
    
    @POST("downloadG")
    @FormUrlEncoded
    Observable<String> downloadPicture(
        @Field("user_id") String user_id);
    
    @POST("uploadR")
    @FormUrlEncoded
    Observable<String> uploadRestaurant(
        @Field("user_id") String user_id,
        @Field("restaurant_list") String restaurant_list,
        @Field("option_list") String option_list);
    
    @POST("downloadR")
    @FormUrlEncoded
    Observable<String> downloadRestaurant(
        @Field("user_id") String user_id);
}
