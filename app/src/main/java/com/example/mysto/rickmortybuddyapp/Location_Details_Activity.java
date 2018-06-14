package com.example.mysto.rickmortybuddyapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.Location;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Location_Details_Activity extends AppCompatActivity {

    Location location_details;

    ImageView location_details_img_fullsize;
    ImageView location_details_img;

    TextView location_details_name;
    TextView location_details_dimension;
    TextView location_details_type;

    Toolbar toolbar;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__details_);

        extras = getIntent().getExtras();

        location_details_img_fullsize = findViewById(R.id.location_details_img_fullsize);
        location_details_img = findViewById(R.id.location_details_img);
        location_details_name = findViewById(R.id.location_details_name);
        location_details_dimension = findViewById(R.id.location_details_dimension);
        location_details_type = findViewById(R.id.location_details_type);

        toolbar = findViewById(R.id.location_details_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_accent);


        if(extras != null) {
            location_details = (Location) extras.getSerializable("location_details");

            location_details_name.setText(location_details.getName());
            location_details_dimension.setText(location_details.getDimension());
            location_details_type.setText(location_details.getType());

            Picasso.with(this)
                    .load(location_details.getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.no_image)
                    .into(location_details_img_fullsize, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getParent())
                                    .load(location_details.getImage())
                                    .placeholder((R.drawable.ic_launcher_background))
                                    .error(R.drawable.no_image)
                                    .into(location_details_img_fullsize, new Callback() {
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
                    .load(location_details.getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.no_image)
                    .into(location_details_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getParent())
                                    .load(location_details.getImage())
                                    .placeholder((R.drawable.ic_launcher_background))
                                    .error(R.drawable.no_image)
                                    .into(location_details_img, new Callback() {
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
