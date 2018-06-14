package com.example.mysto.rickmortybuddyapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Personnage_Details_Activity extends AppCompatActivity {

    Character personnage_details;

    ImageView personnage_details_img;
    TextView personnage_details_name;

    TextView personnage_details_status;
    TextView personnage_details_species;
    TextView personnage_details_gender;

    TextView personnage_details_origin;
    TextView personnage_details_last_location;

    Toolbar toolbar;

    Bundle extras;


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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent);

        if(extras != null) {
            personnage_details = (Character) extras.getSerializable("personnage_details");

            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttp3Downloader(this));

            personnage_details_name.setText(personnage_details.getName());
            personnage_details_status.setText(personnage_details.getStatus());
            personnage_details_species.setText(personnage_details.getSpecies());
            personnage_details_gender.setText(personnage_details.getGender());
            personnage_details_origin.setText(personnage_details.getOrigin().getName());
            personnage_details_last_location.setText(personnage_details.getLocation().getName());

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
