package com.example.mysto.rickmortybuddyapp.Fragments.Locations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Locations.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.RawLocationsServerResponse;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.network.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.RetrofitClientInstance;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Lieux extends android.support.v4.app.Fragment {

    View view;
    RawLocationsServerResponse listLocations;
    ProgressBar progressBar;
    Gson gson = new Gson();

    public Fragment_Lieux() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.lieux_fragment, container, false);
        progressBar = view.findViewById(R.id.progressbar);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("Episodes_List", null);
        progressBar.setVisibility(View.INVISIBLE);

        if(json != null) {

            listLocations = gson.fromJson(json, RawLocationsServerResponse.class);

            RecyclerView rv_locations = view.findViewById(R.id.lieuxRecyclerView);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listLocations);
            rv_locations.setLayoutManager(new LinearLayoutManager(view.getContext()));
            rv_locations.setAdapter(adapter);

        } else {

            progressBar.setVisibility(View.VISIBLE);

            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

            Call<RawLocationsServerResponse> call = service.getAllLocations();

            call.enqueue(new Callback<RawLocationsServerResponse>() {
                @Override
                public void onResponse(Call<RawLocationsServerResponse> call, Response<RawLocationsServerResponse> response) {

                    progressBar.setVisibility(View.INVISIBLE);
                    listLocations = response.body();

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
                    sharedPreferences.edit()
                            .putString("Episodes_List", gson.toJson(listLocations))
                            .apply();

                    RecyclerView rv_locations = view.findViewById(R.id.lieuxRecyclerView);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listLocations);
                    rv_locations.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    rv_locations.setAdapter(adapter);

                }

                @Override
                public void onFailure(Call<RawLocationsServerResponse> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(view.getContext(), "Impossible de joindre le serveur, réessayer ultérieurement", Toast.LENGTH_SHORT).show();
                }
            });

        }

        return view;

    }
}
