package com.androidtask.networking;


import com.androidtask.model.CountriesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiService {

    //*****************   Contacts Method    *****************
    @GET("/rest/v1/all")
    Call<List<CountriesModel>> getCountries();

}