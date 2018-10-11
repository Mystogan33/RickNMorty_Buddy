package com.example.mysto.rickmortybuddyapp.location_details;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.Location;
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

public class Location_Details_Activity extends AppCompatActivity {

    Location location_details;

    ImageView location_details_img_fullsize;

    TextView location_details_name;
    TextView location_details_dimension;
    TextView location_details_type;

    Gson gson;
    GetDataService service;

    Toolbar toolbar;

    RecyclerView recyclerView;
    RecyclerViewEpisodesCharactersAdapter adapter;
    List<String> listURLCharacters;
    List<Character> listCharacters;

    Bundle extras;

    public Location_Details_Activity() {

        gson = new Gson();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__details_);

        extras = getIntent().getExtras();

        location_details_img_fullsize = findViewById(R.id.location_details_img_fullsize);
        location_details_name = findViewById(R.id.location_details_name);
        location_details_dimension = findViewById(R.id.location_details_dimension);
        location_details_type = findViewById(R.id.location_details_type);

        recyclerView = findViewById(R.id.location_details_recyclerview);

        toolbar = findViewById(R.id.location_details_toolbar);
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
            location_details = (Location) extras.getSerializable("location_details");

            location_details_name.setText(location_details.getName());
            location_details_dimension.setText(location_details.getDimension());
            location_details_type.setText(location_details.getType());

            listURLCharacters = location_details.getResidents();
            listCharacters = new ArrayList<>();

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

            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                supportPostponeEnterTransition();
            }

            Picasso.with(this)
                    .load(location_details.getImage())
                    .fit()
                    .centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.no_image)
                    .into(location_details_img_fullsize, new Callback() {
                        @Override
                        public void onSuccess() {

                            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                                supportStartPostponedEnterTransition();
                            }
                        }

                        @Override
                        public void onError() {
                            Picasso.with(getParent())
                                    .load(location_details.getImage())
                                    .fit()
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .error(R.drawable.no_image)
                                    .into(location_details_img_fullsize, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                                                supportStartPostponedEnterTransition();
                                            }

                                        }

                                        @Override
                                        public void onError() {

                                            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                                                supportStartPostponedEnterTransition();
                                            }

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
