package com.example.mysto.rickmortybuddyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Episode;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Episode_Details_Activity extends AppCompatActivity {

    Episode episode_details;

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
            episode_details = (Episode) extras.getSerializable("episode_details");

            episode_details_season.setText(episode_details.getEpisode());
            episode_details_name.setText(episode_details.getName());
            episode_details_air_date.setText(episode_details.getAirDate());

            Picasso.with(this)
                    .load(episode_details.getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.no_data)
                    .into(episode_details_img_fullsize, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getParent())
                                    .load(episode_details.getImage())
                                    .placeholder((R.drawable.ic_launcher_background))
                                    .error(R.drawable.no_data)
                                    .into(episode_details_img_fullsize, new Callback() {
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
                    .load(episode_details.getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.no_image)
                    .into(episode_details_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getParent())
                                    .load(episode_details.getImage())
                                    .placeholder((R.drawable.ic_launcher_background))
                                    .error(R.drawable.no_image)
                                    .into(episode_details_img, new Callback() {
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
}
