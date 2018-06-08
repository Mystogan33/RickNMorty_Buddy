package com.example.mysto.rickmortybuddyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.Location;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class Location_Details_Activity extends AppCompatActivity {

    Location location_details;

    ImageView location_details_img_fullsize;
    ImageView location_details_img;

    TextView location_details_name;
    TextView location_details_dimension;
    TextView location_details_type;

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


        if(extras != null) {
            location_details = (Location) extras.getSerializable("location_details");

            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttp3Downloader(this));

            builder.build().load(location_details.getImage())
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(location_details_img_fullsize);

            builder.build().load(location_details.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(location_details_img);

            location_details_name.setText(location_details.getName());
            location_details_dimension.setText(location_details.getDimension());
            location_details_type.setText(location_details.getType());

        }
    }
}
