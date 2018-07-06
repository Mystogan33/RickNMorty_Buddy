package com.example.mysto.rickmortybuddyapp.episode_details;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dailymotion.android.player.sdk.PlayerWebView;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Episode;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.adapters.RecyclerViewEpisodesCharactersAdapter;
import com.example.mysto.rickmortybuddyapp.network.RickNMortyAPI.GetDataService;
import com.example.mysto.rickmortybuddyapp.network.RickNMortyAPI.RetrofitClientInstance;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Episode_Details_Activity extends AppCompatActivity {

    Episode episode_details;

    ImageView episode_details_img_fullsize;
    ImageView episode_details_img;

    private PlayerWebView webView;

    TextView episode_details_season;
    TextView episode_details_name;
    TextView episode_details_air_date;
    TextView episode_details_description;

    RecyclerView recyclerView;
    RecyclerViewEpisodesCharactersAdapter adapter;
    List<String> listURLCharacters;
    List<Character> listCharacters;

    RelativeLayout relativeLayout;

    Toolbar toolbar;

    Bundle extras;

    GetDataService service;
    Gson gson;


    public Episode_Details_Activity() {
        gson = new Gson();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode__details_);

        extras = getIntent().getExtras();

        episode_details_img_fullsize = findViewById(R.id.episode_details_img_fullsize);
        episode_details_img = findViewById(R.id.episode_details_img);
        episode_details_season = findViewById(R.id.episode_details_season);
        episode_details_name = findViewById(R.id.episode_details_name);
        episode_details_air_date = findViewById(R.id.episode_details_air_date);
        episode_details_description = findViewById(R.id.episode_details_description);
        recyclerView = findViewById(R.id.episode_details_recyclerview);
        toolbar = findViewById(R.id.episode_details_toolbar);
        webView = findViewById(R.id.webView);
        webView.load("x6fb3x9");

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent);

        listCharacters = new ArrayList<>();
        adapter = new RecyclerViewEpisodesCharactersAdapter(listCharacters, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.setAdapter(adapter);

        if(extras != null) {
            episode_details = (Episode) extras.getSerializable("episode_details");

            episode_details_season.setText(episode_details.getEpisode());
            episode_details_name.setText(episode_details.getName());
            episode_details_air_date.setText(episode_details.getAirDate());
            episode_details_description.setText(episode_details.getDescription());
            listURLCharacters = episode_details.getCharacters();
            listCharacters = new ArrayList<>();

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();

            final AppCompatActivity app = this;

            for(String characterUrl : listURLCharacters) {

                final String id = characterUrl.split("/character/")[1];
               Call<Character> call = service.getPersonnageById(Integer.valueOf(id));

                call.enqueue(new retrofit2.Callback<Character>() {
                    @Override
                    public void onResponse(Call<Character> call, Response<Character> response) {
                        listCharacters.add(response.body());
                        adapter = new RecyclerViewEpisodesCharactersAdapter(listCharacters, app);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<Character> call, Throwable t) {

                    }
                });

            }

            Picasso.with(this)
                    .load(episode_details.getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .error(R.drawable.no_data)
                    .into(episode_details_img_fullsize, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            Picasso.with(getParent())
                                    .load(episode_details.getImage())
                                    .fit()
                                    .noFade()
                                    .centerCrop()
                                    .error(R.drawable.no_data)
                                    .into(episode_details_img_fullsize, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    });

            Picasso.with(this)
                    .load(episode_details.getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .error(R.drawable.no_image)
                    .into(episode_details_img, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            Picasso.with(getParent())
                                    .load(episode_details.getImage())
                                    .fit()
                                    .noFade()
                                    .centerCrop()
                                    .error(R.drawable.no_image)
                                    .into(episode_details_img, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError() {
                                        }
                                    });
                        }
                    });

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
