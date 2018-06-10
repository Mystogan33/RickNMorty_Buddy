package com.example.mysto.rickmortybuddyapp.Fragments.Characters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.RawCharactersServerResponse;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.network.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.RetrofitClientInstance;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Personnages extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    RawCharactersServerResponse listPersonnages;
    Gson gson;
    RecyclerView rv_personnages;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerViewAdapter adapter;

    public Fragment_Personnages() {

        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.personnages_fragment, container, false);

        // RecyclerView
        rv_personnages = view.findViewById(R.id.personnagesRecyclerView);

        // Refresh Layout
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("Characters_List", null);

        if(json != null) {

            listPersonnages = gson.fromJson(json, RawCharactersServerResponse.class);

            adapter = new RecyclerViewAdapter(view.getContext(), listPersonnages);
            rv_personnages.setLayoutManager(new GridLayoutManager(view.getContext(),2));
            rv_personnages.setAdapter(adapter);

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

            Call<RawCharactersServerResponse> call = service.getAllPersonnages();

            call.enqueue(new Callback<RawCharactersServerResponse>() {
                @Override
                public void onResponse(Call<RawCharactersServerResponse> call, Response<RawCharactersServerResponse> response) {

                    listPersonnages = response.body();
                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
                    sharedPreferences.edit()
                            .putString("Characters_List", gson.toJson(listPersonnages))
                            .apply();

                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listPersonnages);
                    rv_personnages.setLayoutManager(new GridLayoutManager(view.getContext(),2));
                    rv_personnages.setAdapter(adapter);

                    mSwipeRefreshLayout.setRefreshing(false);

                }

                @Override
                public void onFailure(Call<RawCharactersServerResponse> call, Throwable t) {

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
