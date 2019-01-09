package com.sani.shaheed.mymedicalproject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServices {
    @FormUrlEncoded
    @POST("phone/fetchAll.php")
    Call<Medicine> fetchAll(@Field("_ID") int id,
                            @Field("medName") String medName,
                            @Field("medDescription") String medDescription,
                            @Field("medInterval") String medInterval,
                            @Field("dosage") String dosage,
                            @Field("entryDate") String entryDate,
                            @Field("alarmOnOff") String alarmOnOff);

    @FormUrlEncoded
    @POST("phone/register.php")
    Call<MSG> registrationFormInfo(@Field("email") String email,
                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("phone/login.php")
    Call<MSG> loginFormInfo(@Field("email") String email,
                            @Field("password") String password);
}
