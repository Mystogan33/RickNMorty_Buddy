package com.example.mysto.rickmortybuddyapp.Fragments.Episodes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.RawEpisodesServerResponse;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.network.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.RetrofitClientInstance;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Episodes extends android.support.v4.app.Fragment {

    View view;
    RawEpisodesServerResponse listEpisodes;
    ProgressBar progressBar;
    Gson gson = new Gson();

    public Fragment_Episodes() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.episodes_fragment, container, false);
        progressBar = view.findViewById(R.id.progressbar);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("Episodes_List", null);
        progressBar.setVisibility(View.INVISIBLE);

        if(json != null) {

            listEpisodes = gson.fromJson(json, RawEpisodesServerResponse.class);

            RecyclerView rv_episodes = view.findViewById(R.id.episodesRecyclerView);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listEpisodes);
            rv_episodes.setLayoutManager(new GridLayoutManager(view.getContext(),3));
            rv_episodes.setAdapter(adapter);

        } else {

            progressBar.setVisibility(View.VISIBLE);

            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

            Call<RawEpisodesServerResponse> call = service.getAllEpisodes();

            call.enqueue(new Callback<RawEpisodesServerResponse>() {
                @Override
                public void onResponse(Call<RawEpisodesServerResponse> call, Response<RawEpisodesServerResponse> response) {

                    progressBar.setVisibility(View.INVISIBLE);
                    listEpisodes = response.body();

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
                    sharedPreferences.edit()
                            .putString("Episodes_List", gson.toJson(listEpisodes))
                            .apply();

                    RecyclerView rv_episodes = view.findViewById(R.id.episodesRecyclerView);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listEpisodes);
                    rv_episodes.setLayoutManager(new GridLayoutManager(view.getContext(),3));
                    rv_episodes.setAdapter(adapter);

                }

                @Override
                public void onFailure(Call<RawEpisodesServerResponse> call, Throwable t) {

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(view.getContext(), "Impossible de joindre le serveur, réessayer ultérieurement", Toast.LENGTH_SHORT).show();

                }
            });
        }

        return view;
    }
}
