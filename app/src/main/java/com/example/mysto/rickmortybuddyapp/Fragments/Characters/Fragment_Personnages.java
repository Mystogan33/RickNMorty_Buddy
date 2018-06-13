package com.example.mysto.rickmortybuddyapp.Fragments.Characters;

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
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.RawCharactersServerResponse;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.network.RickNMortyAPI.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.RickNMortyAPI.RetrofitClientInstance;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Personnages extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    RawCharactersServerResponse rawPersonnagesResponse;
    List<Character> listPersonnages;
    Gson gson;
    RecyclerView rv_personnages;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerViewAdapter adapter;
    SearchView searchViewCharacter;
    GetDataService service;
    SharedPreferences sharedPreferences;

    public Fragment_Personnages() {

        gson = new Gson();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the view
        view = inflater.inflate(R.layout.personnages_fragment, container, false);
        // RecyclerView
        rv_personnages = view.findViewById(R.id.personnagesRecyclerView);
        rv_personnages.setLayoutManager(new GridLayoutManager(view.getContext(),2));
        rv_personnages.setAdapter(adapter);

        sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);

        // Refresh Layout
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.requestFocus();
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // SearchView
        searchViewCharacter = view.findViewById(R.id.searchViewQuery);
        searchViewCharacter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               if(!searchViewCharacter.isIconified()) {
                   searchViewCharacter.setIconified(true);
               }

               return false;
            }

            @Override
            public boolean onQueryTextChange(String userInput) {
                final List<Character> filterCharacterList = filter(rawPersonnagesResponse.getResults(), userInput);
                adapter.setFilter(filterCharacterList);
                return true;
            }
        });

        // Sequences of colors from the loading circle
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,android.R.color.holo_orange_dark,android.R.color.holo_blue_dark);
        // Background color for the loading
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark);

        listPersonnages = new ArrayList<>();
        adapter = new RecyclerViewAdapter(view.getContext(), listPersonnages);
        rv_personnages.setAdapter(adapter);

        // Get local Data for Character if exist
        String json = sharedPreferences.getString("Characters_List", null);

        if(json != null) {

            rawPersonnagesResponse = gson.fromJson(json, RawCharactersServerResponse.class);
            listPersonnages = rawPersonnagesResponse.getResults();

            adapter = new RecyclerViewAdapter(view.getContext(), listPersonnages);
            rv_personnages.setLayoutManager(new GridLayoutManager(view.getContext(),2));
            rv_personnages.setAdapter(adapter);


        } else {

            // Launch sequence for remote data
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

            Call<RawCharactersServerResponse> call = service.getAllPersonnagesFromPage(1);

            call.enqueue(new Callback<RawCharactersServerResponse>() {
                @Override
                public void onResponse(Call<RawCharactersServerResponse> call, Response<RawCharactersServerResponse> response) {

                    rawPersonnagesResponse = response.body();
                    listPersonnages = rawPersonnagesResponse.getResults();

                    final Integer numberOfPages = rawPersonnagesResponse.getInfo().getPages();

                    adapter = new RecyclerViewAdapter(view.getContext(), listPersonnages);
                    rv_personnages.setLayoutManager(new GridLayoutManager(view.getContext(),2));
                    rv_personnages.setAdapter(adapter);


                    Toast.makeText(view.getContext(), "Vos données sont désormais sauvegardées", Toast.LENGTH_SHORT).show();

                    if(numberOfPages > 1) {

                        for(int i = 2; i <= numberOfPages; i++) {

                            Call<RawCharactersServerResponse> additionalCall = service.getAllPersonnagesFromPage(i);

                            additionalCall.enqueue(new Callback<RawCharactersServerResponse>() {
                                @Override
                                public void onResponse(Call<RawCharactersServerResponse> call, Response<RawCharactersServerResponse> response) {

                                    listPersonnages.addAll(response.body().getResults());
                                    adapter = new RecyclerViewAdapter(view.getContext(), listPersonnages);
                                    adapter.notifyDataSetChanged();
                                    rv_personnages.setAdapter(adapter);
                                    rawPersonnagesResponse.setResults(listPersonnages);

                                    sharedPreferences.edit()
                                            .putString("Characters_List", gson.toJson(rawPersonnagesResponse))
                                            .apply();

                                }

                                @Override
                                public void onFailure(Call<RawCharactersServerResponse> call, Throwable t) {
                                    Toast.makeText(view.getContext(), "Un soucis s'est produit lors de la récupération du reste des personnages", Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                    } else {

                        sharedPreferences.edit()
                                .putString("Characters_List", gson.toJson(rawPersonnagesResponse))
                                .apply();
                    }

                    mSwipeRefreshLayout.setRefreshing(false);

                }

                @Override
                public void onFailure(Call<RawCharactersServerResponse> call, Throwable t) {

                    Toast.makeText(view.getContext(), "Impossible de joindre le serveur, réessayer ultérieurement", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);

                }
            });

    }

    private List<Character> filter(List<Character> pl, String query) {

        query = query.toLowerCase();
        final List<Character> filteredList = new ArrayList<>();

        for(Character model : pl) {
            final String name = model.getName().toLowerCase();
            final String status = model.getStatus().toLowerCase();
            final String gender = model.getGender().toLowerCase();
            final String origin = model.getOrigin().getName().toLowerCase();
            final String last_location = model.getLocation().getName().toLowerCase();

            if(name.contains(query) || status.contains(query) || gender.equals(query) || origin.contains(query) || last_location.contains(query)) {
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
