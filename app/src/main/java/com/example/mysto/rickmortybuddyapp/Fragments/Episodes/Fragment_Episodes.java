package com.example.mysto.rickmortybuddyapp.Fragments.Episodes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Fragment_Episodes extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    RawEpisodesServerResponse listEpisodes;
    Gson gson;
    RecyclerView rv_episodes;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerViewAdapter adapter;

    public Fragment_Episodes() {
        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.episodes_fragment, container, false);

        // RecyclerView
        rv_episodes = view.findViewById(R.id.episodesRecyclerView);

        // Refresh Layout
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("Episodes_List", null);

        if(json != null) {

            listEpisodes = gson.fromJson(json, RawEpisodesServerResponse.class);

            adapter = new RecyclerViewAdapter(view.getContext(), listEpisodes);
            rv_episodes.setLayoutManager(new GridLayoutManager(view.getContext(),3));
            rv_episodes.setAdapter(adapter);

        } else {

            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {

                    mSwipeRefreshLayout.setRefreshing(true);

                    loadRecyclerViewData();

                }
            });
        }

        return view;
    }

    public void loadRecyclerViewData() {

        mSwipeRefreshLayout.setRefreshing(true);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<RawEpisodesServerResponse> call = service.getAllEpisodes();

        call.enqueue(new Callback<RawEpisodesServerResponse>() {
            @Override
            public void onResponse(Call<RawEpisodesServerResponse> call, Response<RawEpisodesServerResponse> response) {

                listEpisodes = response.body();
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
                sharedPreferences.edit()
                        .putString("Episodes_List", gson.toJson(listEpisodes))
                        .apply();

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listEpisodes);
                rv_episodes.setLayoutManager(new GridLayoutManager(view.getContext(),3));
                rv_episodes.setAdapter(adapter);

                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<RawEpisodesServerResponse> call, Throwable t) {

                Toast.makeText(view.getContext(), "Impossible de joindre le serveur, réessayer ultérieurement", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }
}
