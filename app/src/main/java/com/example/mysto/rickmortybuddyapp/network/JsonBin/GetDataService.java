package com.example.mysto.rickmortybuddyapp.network.JsonBin;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.RawCharactersServerResponse;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.RawEpisodesServerResponse;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.RawLocationsServerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("/b/5b0568367a973f4ce5784514/1")
    Call<RawLocationsServerResponse> getAllLocations();

    @GET("/b/5b1e327ec83f6d4cc734b197/1")
    Call<RawEpisodesServerResponse> getAllEpisodes();

}
