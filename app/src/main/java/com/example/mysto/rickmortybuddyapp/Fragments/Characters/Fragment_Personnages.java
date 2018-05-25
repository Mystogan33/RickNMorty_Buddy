package com.example.mysto.rickmortybuddyapp.Fragments.Characters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Characters;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Location;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Origin;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Result;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.network.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Personnages extends android.support.v4.app.Fragment {

    View view;
    Characters listPersonnages;
    ProgressBar progressBar;

    public Fragment_Personnages() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.personnages_fragment, container, false);

        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<Characters> call = service.getAllPersonnages();

        call.enqueue(new Callback<Characters>() {
            @Override
            public void onResponse(Call<Characters> call, Response<Characters> response) {

                progressBar.setVisibility(View.INVISIBLE);
                listPersonnages = response.body();

                RecyclerView rv_episodes = view.findViewById(R.id.personnagesRecyclerView);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listPersonnages);
                rv_episodes.setLayoutManager(new GridLayoutManager(view.getContext(),2));
                rv_episodes.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Characters> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(view.getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
}
