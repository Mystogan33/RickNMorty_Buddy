package com.example.mysto.rickmortybuddyapp.network;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.RawCharactersServerResponse;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.RawEpisodesServerResponse;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.RawLocationsServerResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("/b/5b05486ec83f6d4cc7349c0d")
    Call<RawCharactersServerResponse> getAllPersonnages();

    @GET("/b/5b0568367a973f4ce5784514")
    Call<RawLocationsServerResponse> getAllLocations();

    @GET("/b/5b0575b1c2e3344ccd96bf34")
    Call<RawEpisodesServerResponse> getAllEpisodes();

}
