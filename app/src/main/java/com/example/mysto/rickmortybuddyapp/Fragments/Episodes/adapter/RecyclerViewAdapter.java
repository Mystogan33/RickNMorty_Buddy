package com.example.mysto.rickmortybuddyapp.Fragments.Episodes.adapter;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.core.util.Pair;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.episode_details.Episode_Details_Activity;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Episode;
import com.example.mysto.rickmortybuddyapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private Fragment mContext;
    private List<Episode> listEpisodes;

    public RecyclerViewAdapter(Fragment mContext, List<Episode> mData) {
        this.mContext = mContext;
        this.listEpisodes = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext.getContext());

        view = mInflater.inflate(R.layout.episodes_fragment_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.episode_fragment_item_name.setText(listEpisodes.get(position).getName());
        holder.episode_fragment_item_season.setText(listEpisodes.get(position).getEpisode());


        final ImageView imageView = holder.episode_fragment_item__img;

        Picasso.with(mContext.getActivity())
                .load(listEpisodes.get(position).getImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.no_image)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext.getActivity())
                                .load(listEpisodes.get(position).getImage())
                                .fit()
                                .centerCrop()
                                .placeholder(R.drawable.ic_launcher_background)
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
                Intent intent = new Intent(mContext.getActivity(), Episode_Details_Activity.class);
                intent.putExtra("episode_details", listEpisodes.get(position));

                // Check if we're running on Android 5.0 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    Pair<View, String> p2 = Pair.create((View) holder.episode_fragment_item__img, "imageEpisode");

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext.getActivity(), p2);
                    mContext.startActivity(intent, optionsCompat.toBundle());

                } else {
                    mContext.startActivity(intent);
                }
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
