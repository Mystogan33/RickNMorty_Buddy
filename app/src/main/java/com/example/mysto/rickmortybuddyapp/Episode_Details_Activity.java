package com.example.mysto.rickmortybuddyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Result;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class Episode_Details_Activity extends AppCompatActivity {

    Result episode_details;

    ImageView episode_details_img_fullsize;
    ImageView episode_details_img;

    TextView episode_details_season;
    TextView episode_details_name;
    TextView episode_details_air_date;
    TextView episode_details_description;

    Bundle extras;



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


        if(extras != null) {
            episode_details = (Result) extras.getSerializable("episode_details");

            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttp3Downloader(this));

            builder.build().load(episode_details.getImage())
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(episode_details_img_fullsize);

            builder.build().load(episode_details.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(episode_details_img);

            episode_details_season.setText(episode_details.getEpisode());
            episode_details_name.setText(episode_details.getName());
            episode_details_air_date.setText(episode_details.getAirDate());

        }

    }
}
