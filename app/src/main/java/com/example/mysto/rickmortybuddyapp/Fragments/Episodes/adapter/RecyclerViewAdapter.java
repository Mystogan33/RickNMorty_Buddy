package com.example.mysto.rickmortybuddyapp.Fragments.Episodes.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Episode_Details_Activity;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Episode;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.RawEpisodesServerResponse;
import com.example.mysto.rickmortybuddyapp.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private Context mContext;
    private List<Episode> listEpisodes;

    public RecyclerViewAdapter(Context mContext, List<Episode> mData) {
        this.mContext = mContext;
        this.listEpisodes = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.episodes_fragment_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.episode_fragment_item_name.setText(listEpisodes.get(position).getName());
        holder.episode_fragment_item_season.setText(listEpisodes.get(position).getEpisode());


        final ImageView imageView = holder.episode_fragment_item__img;

        Picasso.with(mContext.getApplicationContext())
                .load(listEpisodes.get(position).getImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.no_image)
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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
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


    public void setFilter(List<Episode> list) {

        listEpisodes = new ArrayList<>();
        listEpisodes.addAll(list);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView episode_fragment_item_name;
        TextView episode_fragment_item_season;
        ImageView episode_fragment_item__img;

        CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            episode_fragment_item_season = itemView.findViewById(R.id.episode_fragment_item_season);
            episode_fragment_item_name = itemView.findViewById(R.id.episode_fragment_item_name);
            episode_fragment_item__img = itemView.findViewById(R.id.episode_fragment_item__img);

            cardView = itemView.findViewById(R.id.cardview_fragment_item_id);

        }
    }
}
