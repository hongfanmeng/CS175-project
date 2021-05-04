package com.example.cs175_project;

import com.example.cs175_project.model.UploadResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IApi {

    @POST("https://api-sjtu-camp-2021.bytedance.com/homework/invoke/video")
    Call<UploadResponse> submitVideo(@Query("student_id") String studentId,
                                     @Query("user_name") String userName,
                                     @Query("extra_value") String extraValue,
                                     @Part MultipartBody.Part coverImage,
                                     @Part MultipartBody.Part video);

}
