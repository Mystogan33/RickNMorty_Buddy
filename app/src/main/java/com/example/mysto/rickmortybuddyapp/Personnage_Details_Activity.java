package com.example.mysto.rickmortybuddyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Result;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class Personnage_Details_Activity extends AppCompatActivity {

    Result personnage_details;

    ImageView personnage_details_img;
    TextView personnage_details_name;

    TextView personnage_details_status;
    TextView personnage_details_species;
    TextView personnage_details_gender;

    TextView personnage_details_origin;
    TextView personnage_details_last_location;

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


        if(extras != null) {
            personnage_details = (Result) extras.getSerializable("personnage_details");

            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttp3Downloader(this));

            builder.build().load(personnage_details.getImage())
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(personnage_details_img);

            personnage_details_name.setText(personnage_details.getName());
            personnage_details_status.setText(personnage_details.getStatus());
            personnage_details_species.setText(personnage_details.getSpecies());
            personnage_details_gender.setText(personnage_details.getGender());
            personnage_details_origin.setText(personnage_details.getOrigin().getName());
            personnage_details_last_location.setText(personnage_details.getLocation().getName());
        }


    }
}
