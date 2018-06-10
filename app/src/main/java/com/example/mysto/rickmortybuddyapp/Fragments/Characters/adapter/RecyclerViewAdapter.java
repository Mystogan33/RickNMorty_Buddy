package com.example.mysto.rickmortybuddyapp.Fragments.Characters.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.RawCharactersServerResponse;
import com.example.mysto.rickmortybuddyapp.Personnage_Details_Activity;
import com.example.mysto.rickmortybuddyapp.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private Context mContext;
    private RawCharactersServerResponse mData;

    public RecyclerViewAdapter(Context mContext, RawCharactersServerResponse mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.personnages_fragment_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final List<Character> listCharacters = mData.getResults();

        holder.personnage_fragment_item_name.setText(listCharacters.get(position).getName());

        String status = listCharacters.get(position).getStatus();
        holder.personnage_fragment_item_status.setText(status);
        if(status.equals("Dead")) {
            holder.personnage_fragment_item_status.setTextColor(ContextCompat.getColor(mContext, R.color.colorDanger));
        } else {
            holder.personnage_fragment_item_status.setTextColor(ContextCompat.getColor(mContext, R.color.colorValidate));
        }

        holder.personnage_fragment_item_species.setText(listCharacters.get(position).getSpecies());
        holder.personnage_fragment_item_gender.setText(listCharacters.get(position).getGender());
        holder.personnage_fragment_item_origin.setText(listCharacters.get(position).getOrigin().getName());
        holder.personnage_fragment_item_last_location.setText(listCharacters.get(position).getLocation().getName());

        final ImageView imageView = holder.personnage_fragment_item__img;

        Picasso.with(mContext)
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
                        Picasso.with(mContext)
                                .load(listCharacters.get(position).getImage())
                                .placeholder((R.drawable.ic_launcher_background))
                                .error(R.drawable.no_data)
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
                Intent intent = new Intent(mContext, Personnage_Details_Activity.class);
                intent.putExtra("personnage_details", listCharacters.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.getResults().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView personnage_fragment_item__img;
        TextView personnage_fragment_item_name;
        TextView personnage_fragment_item_status;
        TextView personnage_fragment_item_species;
        TextView personnage_fragment_item_gender;
        TextView personnage_fragment_item_origin;
        TextView personnage_fragment_item_last_location;

        CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            personnage_fragment_item__img = itemView.findViewById(R.id.personnage_fragment_item__img);
            personnage_fragment_item_name = itemView.findViewById(R.id.personnage_fragment_item_name);
            personnage_fragment_item_status = itemView.findViewById(R.id.personnage_fragment_item_status);
            personnage_fragment_item_species = itemView.findViewById(R.id.personnage_fragment_item_species);
            personnage_fragment_item_gender = itemView.findViewById(R.id.personnage_fragment_item_gender);
            personnage_fragment_item_origin = itemView.findViewById(R.id.personnage_fragment_item_origin);
            personnage_fragment_item_last_location = itemView.findViewById(R.id.personnage_fragment_item_last_location);

            cardView = itemView.findViewById(R.id.cardview_fragment_item_id);

        }
    }
}
