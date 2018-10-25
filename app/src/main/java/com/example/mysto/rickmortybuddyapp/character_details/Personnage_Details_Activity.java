package com.example.mysto.rickmortybuddyapp.character_details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Episode;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.RawEpisodesServerResponse;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.Location;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.RawLocationsServerResponse;
import com.example.mysto.rickmortybuddyapp.R;
import com.example.mysto.rickmortybuddyapp.adapters.RecyclerViewEpisodesAdapter;
import com.example.mysto.rickmortybuddyapp.location_details.Location_Details_Activity;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Personnage_Details_Activity extends AppCompatActivity {

    Character personnage_details;

    TextView personnage_details_name;
    TextView personnage_details_status;
    TextView personnage_details_species;
    TextView personnage_details_gender;
    TextView personnage_details_origin;
    TextView personnage_details_last_location;

    ImageView personnage_details_img;
    ImageView origin_img;
    ImageView last_location_img;

    RelativeLayout personnage_details_relay_origin;
    RelativeLayout personnage_details_relay_last_location;
    RelativeLayout episodes_recyclerview_relay;
    LinearLayout personnage_details_relay_status;

    CardView cardView;


    Toolbar toolbar;

    Gson gson;
    SharedPreferences sharedPreferences;

    List<String> listURLEpisodes;
    List<Episode> listEpisodes;
    List<Episode> listEpisodesDetails;

    String lastLocationURL;
    Location lastLocationData;
    Integer idLastLocation;
    String originURL;
    List<Location> listLocations;
    Location originData;
    Integer idOrigin;

    RecyclerView recyclerView;
    RecyclerViewEpisodesAdapter adapter;

    Bundle extras;


    public Personnage_Details_Activity() {
        gson = new Gson();
    }

    public void findViews() {

        personnage_details_img = findViewById(R.id.personnage_details_img);
        personnage_details_name = findViewById(R.id.personnage_details__name);
        personnage_details_status = findViewById(R.id.personnage_details_status);
        personnage_details_species = findViewById(R.id.personnage_details_species);
        personnage_details_gender = findViewById(R.id.personnage_details_gender);
        personnage_details_origin = findViewById(R.id.personnage_details_origin);
        personnage_details_last_location = findViewById(R.id.personnage_details_last_location);
        personnage_details_relay_origin = findViewById(R.id.relayOrigin);
        personnage_details_relay_last_location = findViewById(R.id.relayLastLocation);
        personnage_details_relay_status = findViewById(R.id.relayStatus);
        origin_img = findViewById(R.id.origin_img);
        last_location_img = findViewById(R.id.last_location_img);
        cardView = findViewById(R.id.cardview_episodes_of_character);

        recyclerView = findViewById(R.id.episodes_recyclerview);
        episodes_recyclerview_relay = findViewById(R.id.episodes_recyclerview_relay);

        toolbar = findViewById(R.id.toolbar);

    }

    public void setValuesToViews() {
        personnage_details_name.setText(personnage_details.getName());

        String status = personnage_details.getStatus();

        if(status.toLowerCase().equals("dead")) {
            personnage_details_status.setText(getResources().getString(R.string.status_dead));
            personnage_details_relay_status.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDanger));
        } else if(status.toLowerCase().equals("alive")) {
            personnage_details_status.setText(getResources().getString(R.string.status_alive));
        } else {
            personnage_details_status.setText(getResources().getString(R.string.status_unknown));
            personnage_details_relay_status.setBackgroundColor(ContextCompat.getColor(this, R.color.followersBg));
        }

        personnage_details_species.setText(personnage_details.getSpecies());
        personnage_details_gender.setText(personnage_details.getGender());

        String gender = personnage_details.getGender();

        if(gender.toLowerCase().equals("male")) {
            personnage_details_gender.setText(getResources().getString(R.string.gender_male));
        } else if(gender.toLowerCase().equals("female")) {
            personnage_details_gender.setText(getResources().getString(R.string.gender_female));
        } else {
            personnage_details_gender.setText(getResources().getString(R.string.gender_unknown));
        }

        personnage_details_last_location.setText(personnage_details.getLocation().getName());
        personnage_details_origin.setText(personnage_details.getOrigin().getName());
    }

    public void initActionBar() {

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent);

    }

    public void loadImage(final String imgUrl, final ImageView imgView) {

        Picasso.with(this)
                .load(imgUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.no_image)
                .into(imgView, new Callback() {
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onError() {
                        Picasso.with(getParent())
                                .load(imgUrl)
                                .placeholder((R.drawable.ic_launcher_background))
                                .error(R.drawable.no_image)
                                .into(imgView, new Callback() {
                                    @Override
                                    public void onSuccess() {}
                                    @Override
                                    public void onError() {}
                                });
                    }
                });

    }

    public void searchForLocations() {

        for(Location location: listLocations) {

            if(location.getId().equals(idLastLocation)) {
                lastLocationData = location;
                personnage_details_last_location.setText(lastLocationData.getName());

                this.loadImage(lastLocationData.getImage(), last_location_img);

                personnage_details_relay_last_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getApplicationContext(), Location_Details_Activity.class);
                        intent.putExtra("location_details", lastLocationData);
                        startActivity(intent);

                    }
                });

            }

            if(location.getId().equals(idOrigin)) {
                originData = location;
                personnage_details_origin.setText(originData.getName());

                this.loadImage(originData.getImage(), origin_img);

                personnage_details_relay_origin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Location_Details_Activity.class);
                        intent.putExtra("location_details", originData);
                        startActivity(intent);
                    }
                });
            }

        }

    }

    public void searchForEpisodes() {

        for(String episodeUrl: listURLEpisodes) {

            final String id = episodeUrl.split("/episode/")[1];

            for(Episode episode: listEpisodes) {

                if(episode.getId().equals(Integer.valueOf(id))) {
                    listEpisodesDetails.add(episode);
                    adapter.refreshData(listEpisodesDetails);
                }

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character__details_);

        extras = getIntent().getExtras();

        this.findViews();
        this.initActionBar();

        listEpisodesDetails = new ArrayList<>();
        adapter = new RecyclerViewEpisodesAdapter(listEpisodesDetails, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.setAdapter(adapter);

        if(extras != null) {
            personnage_details = (Character) extras.getSerializable("personnage_details");

            this.setValuesToViews();

            listURLEpisodes = personnage_details.getEpisode();
            lastLocationURL = personnage_details.getLocation().getUrl();
            originURL = personnage_details.getOrigin().getUrl();

            sharedPreferences = getApplicationContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);

            RawLocationsServerResponse locationsData =  gson.fromJson(sharedPreferences.getString("Locations_List", null), RawLocationsServerResponse.class);
            RawEpisodesServerResponse episodesData = gson.fromJson(sharedPreferences.getString("Episodes_List", null), RawEpisodesServerResponse.class);


            if (episodesData != null) listEpisodes = episodesData.getResults();
            if(locationsData != null) listLocations = locationsData.getResults();
            if(lastLocationURL != "") idLastLocation = Integer.valueOf(lastLocationURL.split("/location/")[1]);
            if(originURL != "") idOrigin = Integer.valueOf(originURL.split("/location/")[1]);

            this.searchForLocations();

            if(listURLEpisodes != null) {

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) episodes_recyclerview_relay.getLayoutParams();

                if(listURLEpisodes.size() <= 10) { params.height = FrameLayout.LayoutParams.WRAP_CONTENT; params.width = FrameLayout.LayoutParams.MATCH_PARENT; }

                episodes_recyclerview_relay.setLayoutParams(params);

                this.searchForEpisodes();

            }

            this.loadImage(personnage_details.getImage(), personnage_details_img);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
