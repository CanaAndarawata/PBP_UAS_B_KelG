package com.example.tubes_uas.Api;

import com.example.tubes_uas.Model.AllUserResponse;
import com.example.tubes_uas.Model.CateringResponse;
import com.example.tubes_uas.Model.KosResponse;
import com.example.tubes_uas.Model.UserResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("register")
    @FormUrlEncoded
    Call<UserResponse> registerUser( @Field("email") String email,
                                     @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<UserResponse> loginUser(@Field("email") String email,
                                 @Field("password") String password);

    @GET("user")
    Call<AllUserResponse> getAllUser(@Query("data") String data);

    @GET("user/{id}")
    Call<UserResponse> getUserById(@Path("id")int id,
                                   @Query("data") String data);

    @POST("user")
    @FormUrlEncoded
    Call<UserResponse> createUser(@Field("nama") String nama,
                                  @Field("email") String email,
                                  @Field("password") String password);

    @POST("user/update/{id}")
    @FormUrlEncoded
    Call<UserResponse> editUserById(@Path("id")int id,
                                    @Query("data") String data,
                                    @Field("nama") String nama,
                                    @Field("email") String email,
                                    @Field("jenis") String jenis,
                                    @Field("fasilitas") String fasilitas,
                                    @Field("lama") String lama,
                                    @Field("password") String password);

    @POST("user/delete/{id}")
    Call<UserResponse> deleteUserById(@Path("id")int id);


    //CRUD Kos
    @POST("kos")
    @FormUrlEncoded
    Call<KosResponse> createKos( @Field("jenis") String jenis,
                                  @Field("fasilitas") String fasilitas,
                                  @Field("lama") String lama);

    @GET("kos")
    Call<UserResponse> getAllKos(@Query("data") String data);

    @GET("kos/{id}")
    Call<KosResponse> getKosById(@Path("id")int id,
                                   @Query("data") String data);

    @PUT("kos/update/{id}")
    @FormUrlEncoded
    Call<KosResponse> editKosById(@Path("id")int id,
                                    @Field("jenis") String jenis,
                                    @Field("fasilitas") String fasilitas,
                                    @Field("lama") String lama);

    @DELETE("kos/delete/{id}")
    Call<KosResponse> deleteKosById(@Path("id") int id);


    //CRUD Catering
    @POST("catering")
    @FormUrlEncoded
    Call<CateringResponse> createCatering(  @Field("paket") String paket,
                                        @Field("hari") String hari,
                                        @Field("bulan") String bulan);

    @GET("catering")
    Call<UserResponse> getAllCatering(@Query("data") String data);

    @GET("catering/{id}")
    Call<CateringResponse> getCateringById( @Path("id")int id,
                                        @Query("data") String data);

    @PUT("catering/update/{id}")
    @FormUrlEncoded
    Call<CateringResponse> editCateringById(@Path("id")int id,
                                        @Field("paket") String paket,
                                        @Field("hari") String hari,
                                        @Field("bulan") String bulan);

    @DELETE("catering/delete/{id}")
    Call<CateringResponse> deleteCateringById(@Path("id") int id);
}
