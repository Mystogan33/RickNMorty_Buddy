package com.example.mysto.rickmortybuddyapp.Fragments.Characters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

public class Fragment_Personnages extends android.support.v4.app.Fragment {

    View view;
    RawCharactersServerResponse listPersonnages;
    ProgressBar progressBar;
    Gson gson;

    public Fragment_Personnages() {

        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.personnages_fragment, container, false);
        progressBar = view.findViewById(R.id.progressbar);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("Characters_List", null);
        progressBar.setVisibility(View.INVISIBLE);

        if(json != null) {

            listPersonnages = gson.fromJson(json, RawCharactersServerResponse.class);
            Toast.makeText(view.getContext(), "Local data retrieved", Toast.LENGTH_SHORT).show();
            RecyclerView rv_personnages = view.findViewById(R.id.personnagesRecyclerView);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listPersonnages);
            rv_personnages.setLayoutManager(new GridLayoutManager(view.getContext(),2));
            rv_personnages.setAdapter(adapter);

        } else {

            progressBar.setVisibility(View.VISIBLE);

            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

            Call<RawCharactersServerResponse> call = service.getAllPersonnages();

            call.enqueue(new Callback<RawCharactersServerResponse>() {
                @Override
                public void onResponse(Call<RawCharactersServerResponse> call, Response<RawCharactersServerResponse> response) {

                    progressBar.setVisibility(View.INVISIBLE);
                    listPersonnages = response.body();
                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
                    sharedPreferences.edit()
                            .putString("Characters_List", gson.toJson(listPersonnages))
                            .apply();

                    RecyclerView rv_personnages = view.findViewById(R.id.personnagesRecyclerView);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listPersonnages);
                    rv_personnages.setLayoutManager(new GridLayoutManager(view.getContext(),2));
                    rv_personnages.setAdapter(adapter);

                }

                @Override
                public void onFailure(Call<RawCharactersServerResponse> call, Throwable t) {

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(view.getContext(), "Impossible de joindre le serveur, réessayer ultérieurement", Toast.LENGTH_SHORT).show();

                }
            });


        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();



    }
}
