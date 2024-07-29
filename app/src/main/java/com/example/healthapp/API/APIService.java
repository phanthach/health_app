package com.example.healthapp.API;

import com.example.healthapp.Response.ThongTinThoiTietResponse;
import com.example.healthapp.Response.WeatherResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//    Link API: http://api.weatherapi.com/v1/current.json?Key=4239a7bcd21e4b1eb23190251240405&q=HaNoi
    APIService apiService = new Retrofit.Builder()
        .baseUrl("http://api.weatherapi.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(APIService.class);

    APIService apiMapBoxService = new Retrofit.Builder()
            .baseUrl("https://api.mapbox.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    @GET("v1/current.json")
    Call<WeatherResponse> converWeatherData(@Query("Key") String key, @Query("q") String location, @Query("lang") String lang);
    @GET("v1/astronomy.json")
    Call<ThongTinThoiTietResponse> converThongTinThoiTietData(@Query("Key") String key, @Query("q") String location);
}
