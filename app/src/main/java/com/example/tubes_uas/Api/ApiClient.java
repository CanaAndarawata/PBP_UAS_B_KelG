package com.example.tubes_uas.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

//    public static final String BASE_URL ="https://apipbp.ninnanovila.com/api/";
//    public static final String BASE_URL ="https://tubespbp-kelg.000webhostapp.com/api/";
//    public static final String BASE_URL ="https://tubes-pbpg.000webhostapp.com/api/";
    public static final String BASE_URL ="https://pbpkelompokg.xyz/api/";


    public static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
