package com.example.mysto.rickmortybuddyapp.Fragments.Locations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Locations.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.Location;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.RawLocationsServerResponse;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.interfaces.Refreshable;
import com.example.mysto.rickmortybuddyapp.network.JsonBin.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.JsonBin.RetrofitClientInstance;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mysto.rickmortybuddyapp.R.id.locationsRecyclerView;

public class Fragment_Lieux extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Refreshable {

    View view;
    @BindView(locationsRecyclerView)
    RecyclerView rv_locations;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.searchViewQuery)
    SearchView searchViewLocations;

    RawLocationsServerResponse rawLocationsResponse;
    List<Location> listLocations;
    Gson gson;
    RecyclerViewAdapter adapter;
    SharedPreferences sharedPreferences;
    GetDataService service;

    public Fragment_Lieux() {
        gson = new Gson();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.locations_fragment, container, false);
        ButterKnife.bind(this, view);

        listLocations = new ArrayList<>();
        adapter = new RecyclerViewAdapter( this, listLocations);
        rv_locations.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rv_locations.setAdapter(adapter);

        sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);

        // Refresh Layout
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,android.R.color.holo_orange_dark,android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark);

        searchViewLocations.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String userInput) {
                if(!searchViewLocations.isIconified()) searchViewLocations.setIconified(true);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String userInput) {
                final List<Location> filterLocationsList = setFilter(listLocations, userInput);
                adapter.setFilter(filterLocationsList);
                return true;
            }
        });

        String json = sharedPreferences.getString("Locations_List", null);

        if(json != null) {

            rawLocationsResponse = gson.fromJson(json, RawLocationsServerResponse.class);
            listLocations = rawLocationsResponse.getResults();

            Collections.sort(listLocations, new Comparator<Location>() {
                @Override
                public int compare(Location loc2, Location loc1)
                {
                    return  loc2.getName().compareTo(loc1.getName());
                }
            });

            adapter.setFilter(listLocations);

        } else {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    loadRecyclerViewData();
                }
            });
        }

        return view;
    }

    public void loadRecyclerViewData() {

        mSwipeRefreshLayout.setRefreshing(true);

        Call<RawLocationsServerResponse> call = service.getAllLocations();
        call.enqueue(new Callback<RawLocationsServerResponse>() {
            @Override
            public void onResponse(Call<RawLocationsServerResponse> call, Response<RawLocationsServerResponse> response) {
                rawLocationsResponse = response.body();
                listLocations = new ArrayList<>();

                for(Location loc: rawLocationsResponse.getResults()) {
                    String image = loc.getImage();
                    if(image == null || image.equals("unknown")) loc.setImage("https://i.redd.it/lwwt86ci5anz.jpg");
                    listLocations.add(loc);
                }

                Collections.sort(listLocations, new Comparator<Location>() {
                    @Override
                    public int compare(Location loc2, Location loc1)
                    {
                        return  loc2.getName().compareTo(loc1.getName());
                    }
                });

                adapter.setFilter(listLocations);
                sharedPreferences.edit()
                        .putString("Locations_List", gson.toJson(rawLocationsResponse))
                        .apply();

                Toast.makeText(view.getContext(), "Vos données sont désormais sauvegardées", Toast.LENGTH_SHORT).show();

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
            if(name.contains(query) || type.contains(query) || dimension.contains(query)) filteredList.add(model);
        }

        return filteredList;
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }
}
