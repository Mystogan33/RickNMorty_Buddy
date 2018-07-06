package com.example.mysto.rickmortybuddyapp.character_details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.jakewharton.picasso.OkHttp3Downloader;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnage__details_);

        extras = getIntent().getExtras();

        personnage_details_img = findViewById(R.id.personnage_details_img);
        personnage_details_name = findViewById(R.id.personnage_details__name);
        personnage_details_status = findViewById(R.id.personnage_details_status);
        personnage_details_species = findViewById(R.id.personnage_details_species);
        personnage_details_gender = findViewById(R.id.personnage_details_gender);
        personnage_details_origin = findViewById(R.id.personnage_details_origin);
        personnage_details_last_location = findViewById(R.id.personnage_details_last_location);
        personnage_details_relay_origin = findViewById(R.id.relayOrigin);
        personnage_details_relay_last_location = findViewById(R.id.relayLastLocation);
        origin_img = findViewById(R.id.origin_img);
        last_location_img = findViewById(R.id.last_location_img);
        cardView = findViewById(R.id.cardview_episodes_of_character);

        recyclerView = findViewById(R.id.episodes_recyclerview);
        episodes_recyclerview_relay = findViewById(R.id.episodes_recyclerview_relay);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent);

        listEpisodesDetails = new ArrayList<>();
        adapter = new RecyclerViewEpisodesAdapter(listEpisodesDetails, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.setAdapter(adapter);

        if(extras != null) {
            personnage_details = (Character) extras.getSerializable("personnage_details");

            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttp3Downloader(this));

            personnage_details_name.setText(personnage_details.getName());
            personnage_details_status.setText(personnage_details.getStatus());
            personnage_details_species.setText(personnage_details.getSpecies());
            personnage_details_gender.setText(personnage_details.getGender());
            personnage_details_last_location.setText(personnage_details.getLocation().getName());
            personnage_details_origin.setText(personnage_details.getOrigin().getName());

            listURLEpisodes = personnage_details.getEpisode();
            lastLocationURL = personnage_details.getLocation().getUrl();
            originURL = personnage_details.getOrigin().getUrl();


            sharedPreferences = getApplicationContext().getSharedPreferences("APP_DATA", Context.MODE_PRIVATE);
            String jsonLocations = sharedPreferences.getString("Locations_List", null);
            String jsonEpisodes = sharedPreferences.getString("Episodes_List", null);

            RawLocationsServerResponse locationsData =  gson.fromJson(jsonLocations, RawLocationsServerResponse.class);
            RawEpisodesServerResponse episodesData = gson.fromJson(jsonEpisodes, RawEpisodesServerResponse.class);


            if (episodesData != null) {
                listEpisodes = episodesData.getResults();
            }

            if(locationsData != null) {
                listLocations = locationsData.getResults();
            }

            if(lastLocationURL != "") {
                idLastLocation = Integer.valueOf(lastLocationURL.split("/location/")[1]);
            }

            if(originURL != "") {
                idOrigin = Integer.valueOf(originURL.split("/location/")[1]);
            }

            for(Location location: listLocations) {

                if(location.getId().equals(idLastLocation)) {
                    lastLocationData = location;
                    personnage_details_last_location.setText(lastLocationData.getName());

                    Picasso.with(this)
                            .load(lastLocationData.getImage())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder((R.drawable.ic_launcher_background))
                            .error(R.drawable.no_image)
                            .into(last_location_img, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(getParent())
                                            .load(lastLocationData.getImage())
                                            .placeholder((R.drawable.ic_launcher_background))
                                            .error(R.drawable.no_image)
                                            .into(last_location_img, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                }

                                                @Override
                                                public void onError() {
                                                }
                                            });
                                }
                            });

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

                    Picasso.with(this)
                            .load(originData.getImage())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder((R.drawable.ic_launcher_background))
                            .error(R.drawable.no_image)
                            .into(origin_img, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(getParent())
                                            .load(originData.getImage())
                                            .placeholder((R.drawable.ic_launcher_background))
                                            .error(R.drawable.no_image)
                                            .into(origin_img, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                }

                                                @Override
                                                public void onError() {
                                                }
                                            });
                                }
                            });

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

            if(listURLEpisodes != null) {

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) episodes_recyclerview_relay.getLayoutParams();

                if(listURLEpisodes.size() <= 10) {

                    params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                    params.width = FrameLayout.LayoutParams.MATCH_PARENT;

                }

                episodes_recyclerview_relay.setLayoutParams(params);

                for(String episodeUrl: listURLEpisodes) {

                    final String id = episodeUrl.split("/episode/")[1];

                    for(Episode episode: listEpisodes) {

                        if(episode.getId().equals(Integer.valueOf(id))) {
                            listEpisodesDetails.add(episode);
                            adapter = new RecyclerViewEpisodesAdapter(listEpisodesDetails, this);
                            recyclerView.setAdapter(adapter);
                        }

                    }

                }

            }


            Picasso.with(this)
                    .load(personnage_details.getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.no_image)
                    .into(personnage_details_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getParent())
                                    .load(personnage_details.getImage())
                                    .placeholder((R.drawable.ic_launcher_background))
                                    .error(R.drawable.no_image)
                                    .into(personnage_details_img, new Callback() {
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
