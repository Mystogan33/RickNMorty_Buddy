package com.example.mysto.rickmortybuddyapp.Fragments.Characters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.RawCharactersServerResponse;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.network.RickNMortyAPI.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.RickNMortyAPI.RetrofitClientInstance;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    ImageButton imageButton;
    GetDataService service;
    SharedPreferences sharedPreferences;
    PopupMenu popup;
    MenuInflater inflater;

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

        // Button options
        imageButton = view.findViewById(R.id.imageViewSearchMenu);

        listPersonnages = new ArrayList<>();
        adapter = new RecyclerViewAdapter(Fragment_Personnages.this, listPersonnages);
        rv_personnages.setLayoutManager(new GridLayoutManager(view.getContext(),2));
        rv_personnages.setAdapter(adapter);

        sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);

        // Refresh Layout
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
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

        initPopup(imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });

        // Sequences of colors from the loading circle
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,android.R.color.holo_orange_dark,android.R.color.holo_blue_dark);
        // Background color for the loading
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark);

        // Get local Data for Character if exist
        String json = sharedPreferences.getString("Characters_List", null);

        if(json != null) {

            rawPersonnagesResponse = gson.fromJson(json, RawCharactersServerResponse.class);
            listPersonnages = rawPersonnagesResponse.getResults();

            Collections.sort(listPersonnages, new Comparator<Character>() {
                @Override
                public int compare(Character character2, Character character1)
                {

                    return  character2.getName().compareTo(character1.getName());
                }
            });

            adapter = new RecyclerViewAdapter(Fragment_Personnages.this, listPersonnages);
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

                    Collections.sort(listPersonnages, new Comparator<Character>() {
                        @Override
                        public int compare(Character character2, Character character1)
                        {

                            return  character2.getName().compareTo(character1.getName());
                        }
                    });

                    adapter = new RecyclerViewAdapter(Fragment_Personnages.this, listPersonnages);
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

                                    Collections.sort(listPersonnages, new Comparator<Character>() {
                                        @Override
                                        public int compare(Character character2, Character character1)
                                        {

                                            return  character2.getName().compareTo(character1.getName());
                                        }
                                    });

                                    adapter = new RecyclerViewAdapter(Fragment_Personnages.this, listPersonnages);
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
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            });

                        }

                    } else {

                        Collections.sort(listPersonnages, new Comparator<Character>() {
                            @Override
                            public int compare(Character character2, Character character1)
                            {

                                return  character2.getName().compareTo(character1.getName());
                            }
                        });

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

            if((name.contains(query) && popup.getMenu().getItem(0).isChecked())
                    || (status.contains(query) && popup.getMenu().getItem(1).isChecked())
                    || (gender.equals(query) && popup.getMenu().getItem(2).isChecked())
                    || (origin.contains(query) && popup.getMenu().getItem(3).isChecked())
                    || (last_location.contains(query) && popup.getMenu().getItem(4).isChecked()))  filteredList.add(model);
        }

        return filteredList;
    }

    public void initPopup(View v) {

        popup = new PopupMenu(view.getContext(), v);
        inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.filters_characters, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.name:
                        /*if(menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);*/
                        return true;
                    case R.id.status:
                        if(menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);
                        return true;
                    case R.id.gender:
                        if(menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);
                        return true;
                    case R.id.origin:
                        if(menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);
                        return true;
                    case R.id.last_location:
                        if(menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);
                        return true;
                }
                return true;
            }
        });

    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }

}
