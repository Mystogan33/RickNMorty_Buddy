package com.example.mysto.rickmortybuddyapp.network.RickNMortyAPI;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.RawCharactersServerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("character")
    Call<RawCharactersServerResponse>  getAllPersonnagesFromPage(@Query("page") int pageId);

}
