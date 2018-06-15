package com.example.mysto.rickmortybuddyapp.Fragments.Locations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Locations.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.Location;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.RawLocationsServerResponse;
import com.example.mysto.rickmortybuddyapp.R;
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

import static com.example.mysto.rickmortybuddyapp.R.id.locationsRecyclerView;

public class Fragment_Lieux extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    RawLocationsServerResponse listLocations;
    Gson gson;
    RecyclerView rv_locations;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerViewAdapter adapter;

    SearchView searchViewLocations;

    public Fragment_Lieux() {
        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.lieux_fragment, container, false);

        // RecyclerView
        rv_locations = view.findViewById(locationsRecyclerView);

        // Refresh Layout
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,android.R.color.holo_orange_dark,android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark);

        searchViewLocations = view.findViewById(R.id.searchViewQuery);

        searchViewLocations.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!searchViewLocations.isIconified()) {
                    searchViewLocations.setIconified(true);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String userInput) {
                final List<Location> filterLocationsList = setFilter(listLocations.getResults(), userInput);
                adapter.setFilter(filterLocationsList);
                return true;
            }
        });

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("Locations_List", null);

        if(json != null) {

            listLocations = gson.fromJson(json, RawLocationsServerResponse.class);

            Collections.sort(listLocations.getResults(), new Comparator<Location>() {
                @Override
                public int compare(Location loc2, Location loc1)
                {

                    return  loc1.getName().compareTo(loc2.getName());
                }
            });

            adapter = new RecyclerViewAdapter(view.getContext(), listLocations.getResults());
            rv_locations.setLayoutManager(new LinearLayoutManager(view.getContext()));
            rv_locations.setAdapter(adapter);

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

        Call<RawLocationsServerResponse> call = service.getAllLocations();

        call.enqueue(new Callback<RawLocationsServerResponse>() {
            @Override
            public void onResponse(Call<RawLocationsServerResponse> call, Response<RawLocationsServerResponse> response) {

                listLocations = response.body();
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
                sharedPreferences.edit()
                        .putString("Locations_List", gson.toJson(listLocations))
                        .apply();

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listLocations.getResults());
                rv_locations.setLayoutManager(new LinearLayoutManager(view.getContext()));
                rv_locations.setAdapter(adapter);

                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<RawLocationsServerResponse> call, Throwable t) {

                Toast.makeText(view.getContext(), "Impossible de joindre le serveur, réessayer ultérieurement", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private List<Location> setFilter(List<Location> pl, String query) {

        query = query.toLowerCase();
        final List<Location> filteredList = new ArrayList<>();

        for(Location model : pl) {
            final String name = model.getName().toLowerCase();
            final String type = model.getType().toLowerCase();
            final String dimension = model.getDimension().toLowerCase();

            if(name.contains(query) || type.contains(query) || dimension.contains(query)) {
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
