package com.example.mysto.rickmortybuddyapp.episode_details;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class Episode_Details_Activity extends AppCompatActivity {

    @BindView(R.id.episode_details_img_fullsize)
    ImageView episode_details_img_fullsize;
    @BindView(R.id.episode_details_img)
    ImageView episode_details_img;

    @BindView(R.id.webView)
    PlayerWebView webView;

    @BindView(R.id.episode_details_season)
    TextView episode_details_season;
    @BindView(R.id.episode_details_name)
    TextView episode_details_name;
    @BindView(R.id.episode_details_air_date)
    TextView episode_details_air_date;
    @BindView(R.id.episode_details_description)
    TextView episode_details_description;

    @BindView(R.id.episode_details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.episode_details_recyclerview)
    RecyclerView recyclerView;

    Episode episode_details;

    RecyclerViewEpisodesCharactersAdapter adapter;
    List<String> listURLCharacters;
    List<Character> listCharacters;

    Bundle extras;

    GetDataService service;
    Gson gson;

    AppCompatActivity app;


    public Episode_Details_Activity() {
        gson = new Gson();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
    }

    public void initActionBar() {

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent);

    }

    public void setValuesToViews() {
        episode_details_season.setText(episode_details.getEpisode());
        episode_details_name.setText(episode_details.getName());
        episode_details_air_date.setText(episode_details.getAirDate());
        episode_details_description.setText(episode_details.getDescription());
    }

    public void loadCharacters() {

        for(String characterUrl : listURLCharacters) {

            final String id = characterUrl.split("/character/")[1];
            Call<Character> call = service.getPersonnageById(Integer.valueOf(id));

            call.enqueue(new retrofit2.Callback<Character>() {
                @Override
                public void onResponse(Call<Character> call, Response<Character> response) {
                    listCharacters.add(response.body());
                    adapter.refreshData(listCharacters);
                }
                @Override
                public void onFailure(Call<Character> call, Throwable t) {}
            });

        }

    }

    public void loadImage(final String imgUrl, final ImageView imgView) {

        Picasso.with(app)
                .load(imgUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .noFade()
                .centerCrop()
                .error(R.drawable.no_data)
                .into(imgView, new Callback() {
                    @Override
                    public void onSuccess() { supportStartPostponedEnterTransition(); }
                    @Override
                    public void onError() {
                        Picasso.with(app)
                                .load(imgUrl)
                                .fit()
                                .noFade()
                                .centerCrop()
                                .error(R.drawable.no_data)
                                .into(imgView, new Callback() {
                                    @Override
                                    public void onSuccess() { supportStartPostponedEnterTransition(); }

                                    @Override
                                    public void onError() {}
                                });
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode__details_);

        extras = getIntent().getExtras();

        ButterKnife.bind(this);
        this.initActionBar();

        listCharacters = new ArrayList<>();
        adapter = new RecyclerViewEpisodesCharactersAdapter(listCharacters, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.setAdapter(adapter);

        webView.load("x62qgh0");

        if(extras != null) {

            episode_details = (Episode) extras.getSerializable("episode_details");

            this.setValuesToViews();

            listURLCharacters = episode_details.getCharacters();

            app = this;

            this.loadCharacters();

            supportPostponeEnterTransition();

            this.loadImage(episode_details.getImage(), episode_details_img_fullsize);
            this.loadImage(episode_details.getImage(), episode_details_img);

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
