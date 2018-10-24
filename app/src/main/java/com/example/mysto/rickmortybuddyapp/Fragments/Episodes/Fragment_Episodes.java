package com.example.mysto.rickmortybuddyapp.Fragments.Episodes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Episode;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.RawEpisodesServerResponse;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.interfaces.Refreshable;
import com.example.mysto.rickmortybuddyapp.network.JsonBin.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.JsonBin.RetrofitClientInstance;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Episodes extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Refreshable {

    View view;
    RawEpisodesServerResponse listEpisodes;
    Gson gson;
    RecyclerView rv_episodes;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerViewAdapter adapter;
    GetDataService service;
    SearchView searchViewEpisodes;

    public Fragment_Episodes() {

        gson = new Gson();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
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
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,android.R.color.holo_orange_dark,android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark);

        searchViewEpisodes = view.findViewById(R.id.searchViewQuery);

        searchViewEpisodes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!searchViewEpisodes.isIconified()) {
                    searchViewEpisodes.setIconified(true);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String userInput) {
                final List<Episode> filterEpisodesList = setFilter(listEpisodes.getResults(), userInput);
                adapter.setFilter(filterEpisodesList);
                return true;
            }
        });

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("Episodes_List", null);


        adapter = new RecyclerViewAdapter(Fragment_Episodes.this, new ArrayList<Episode>());
        rv_episodes.setLayoutManager(new GridLayoutManager(view.getContext(),3));
        rv_episodes.setAdapter(adapter);

        if(json != null) {

            listEpisodes = gson.fromJson(json, RawEpisodesServerResponse.class);
            adapter.setFilter(listEpisodes.getResults());

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

        Call<RawEpisodesServerResponse> call = service.getAllEpisodes();

        call.enqueue(new Callback<RawEpisodesServerResponse>() {
            @Override
            public void onResponse(Call<RawEpisodesServerResponse> call, Response<RawEpisodesServerResponse> response) {

                listEpisodes = response.body();

                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
                sharedPreferences.edit()
                        .putString("Episodes_List", gson.toJson(listEpisodes))
                        .apply();

                adapter.setFilter(listEpisodes.getResults());

                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<RawEpisodesServerResponse> call, Throwable t) {

                Toast.makeText(view.getContext(), "Impossible de joindre le serveur, réessayer ultérieurement", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private List<Episode> setFilter(List<Episode> pl, String query) {

        query = query.toLowerCase();
        final List<Episode> filteredList = new ArrayList<>();

        for(Episode model : pl) {
            final String name = model.getName().toLowerCase();
            final String episode = model.getEpisode().toLowerCase();

            if(name.contains(query) || episode.contains(query)) {
                filteredList.add(model);
            }

        }

        return filteredList;

    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }
}
