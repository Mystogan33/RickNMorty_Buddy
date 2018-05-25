package com.example.mysto.rickmortybuddyapp.Fragments.Episodes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.adapter.RecyclerViewAdapter;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Episodes;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Result;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.network.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Episodes extends android.support.v4.app.Fragment {

    View view;
    Episodes listEpisodes;
    ProgressBar progressBar;

    /*private List<String> characters = null;*/

    public Fragment_Episodes() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.episodes_fragment, container, false);

        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<Episodes> call = service.getAllEpisodes();

        call.enqueue(new Callback<Episodes>() {
            @Override
            public void onResponse(Call<Episodes> call, Response<Episodes> response) {

                progressBar.setVisibility(View.INVISIBLE);
                listEpisodes = response.body();

                RecyclerView rv_episodes = view.findViewById(R.id.episodesRecyclerView);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), listEpisodes);
                rv_episodes.setLayoutManager(new GridLayoutManager(view.getContext(),3));
                rv_episodes.setAdapter(adapter);

                /* For Dev Purpose
                listEpisodes = new ArrayList<>();

                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1));
                listEpisodes.add(new Result("7 days ago","https://www.ecranlarge.com/uploads/image/001/016/rick-et-morty-photo-rick-et-morty-1016132.jpg","URL_image",characters,"S01EP01","23 janvier 2018","Un episode de fou",1)); */

            }

            @Override
            public void onFailure(Call<Episodes> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(view.getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
}
