package com.example.mysto.rickmortybuddyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.example.mysto.rickmortybuddyapp.character_details.Personnage_Details_Activity;
import com.example.mysto.rickmortybuddyapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewEpisodesCharactersAdapter extends RecyclerView.Adapter<RecyclerViewEpisodesCharactersAdapter.MyViewHolder> {

    List<Character> listCharacters;
    AppCompatActivity mContext;

    public RecyclerViewEpisodesCharactersAdapter(List<Character> listEpisodeCharacters, AppCompatActivity mContext) {
        this.listCharacters = listEpisodeCharacters;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.activity_episode_details_character,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final ImageView imageView = holder.img_character;



        Picasso.with(mContext.getApplicationContext())
                .load(listCharacters.get(position).getImage())
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
                                .load(listCharacters.get(position).getImage())
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
                Intent intent = new Intent(mContext, Personnage_Details_Activity.class);
                intent.putExtra("personnage_details", listCharacters.get(position));

                // Check if we're running on Android 5.0 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, imageView, "imageCharacter");
                    mContext.startActivity(intent, optionsCompat.toBundle());

                } else {
                    // Swap without transition
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listCharacters.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_character;


        public MyViewHolder(View itemView) {
            super(itemView);

            img_character = itemView.findViewById(R.id.img);

        }
    }

}
