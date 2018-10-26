package com.example.mysto.rickmortybuddyapp.location_details;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class Location_Details_Activity extends AppCompatActivity {

    Location location_details;

    @BindView(R.id.location_details_img_fullsize)
    ImageView location_details_img_fullsize;
    @BindView(R.id.location_details_name)
    TextView location_details_name;
    @BindView(R.id.location_details_dimension)
    TextView location_details_dimension;
    @BindView(R.id.location_details_type)
    TextView location_details_type;
    @BindView(R.id.location_details_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.location_details_toolbar)
    Toolbar toolbar;

    Gson gson;
    GetDataService service;

    RecyclerViewEpisodesCharactersAdapter adapter;
    List<String> listURLCharacters;
    List<Character> listCharacters;

    Bundle extras;
    AppCompatActivity app;

    public Location_Details_Activity() {
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
        location_details_name.setText(location_details.getName());
        location_details_dimension.setText(location_details.getDimension());
        location_details_type.setText(location_details.getType());
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
                public void onFailure(Call<Character> call, Throwable t) { }
            });
        }
    }

    public void loadImage(final String imgUrl, final ImageView imgView) {
        Picasso.with(app)
                .load(imgUrl)
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.no_image)
                .into(imgView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }
                    @Override
                    public void onError() {
                        Picasso.with(app)
                                .load(imgUrl)
                                .fit()
                                .centerCrop()
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.no_image)
                                .into(imgView, new Callback() {
                                    @Override
                                    public void onSuccess() { supportStartPostponedEnterTransition(); }
                                    @Override
                                    public void onError() { supportStartPostponedEnterTransition(); }
                                });
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__details_);
        extras = getIntent().getExtras();
        ButterKnife.bind(this);
        this.initActionBar();

        listCharacters = new ArrayList<>();
        adapter = new RecyclerViewEpisodesCharactersAdapter(listCharacters, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.setAdapter(adapter);

        if(extras != null) {
            location_details = (Location) extras.getSerializable("location_details");
            this.setValuesToViews();
            listURLCharacters = location_details.getResidents();
            listCharacters = new ArrayList<>();
            app = this;
            this.loadCharacters();
            supportPostponeEnterTransition();
            this.loadImage(location_details.getImage(), location_details_img_fullsize);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
