package com.example.mysto.rickmortybuddyapp.network.RickNMortyAPI;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.RawCharactersServerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("character")
    Call<RawCharactersServerResponse>  getAllPersonnagesFromPage(@Query("page") int pageId);

    @GET("character/{id}")
    Call<Character> getPersonnageById(@Path("id") int characterId);

}
