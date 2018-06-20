package com.example.mysto.rickmortybuddyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mysto.rickmortybuddyapp.Episode_Details_Activity;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Episode;
import com.example.mysto.rickmortybuddyapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewEpisodesAdapter extends RecyclerView.Adapter<RecyclerViewEpisodesAdapter.MyViewHolder> {

    List<Episode> listEpisodes;
    Context mContext;

    public RecyclerViewEpisodesAdapter(List<Episode> listEpisodes, Context mContext) {
        this.listEpisodes = listEpisodes;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.activity_character_details_episode,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final ImageView imageView = holder.img_episode;



        Picasso.with(mContext.getApplicationContext())
                .load(listEpisodes.get(position).getImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.no_data)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext.getApplicationContext())
                                .load(listEpisodes.get(position).getImage())
                                .placeholder((R.drawable.ic_launcher_background))
                                .error(R.drawable.no_image)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });
                    }
                });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Episode_Details_Activity.class);
                intent.putExtra("episode_details", listEpisodes.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listEpisodes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_episode;


        public MyViewHolder(View itemView) {
            super(itemView);

            img_episode = itemView.findViewById(R.id.img);

        }
    }


}