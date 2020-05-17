package com.example.proyecto_applepie.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                  @Field("name") String name,
                                  @Field("google_id") String google_id,
                                  @Field("paypalmeLink") String paypalmeLink);
}
