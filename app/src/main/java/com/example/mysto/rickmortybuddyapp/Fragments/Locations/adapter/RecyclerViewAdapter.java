package com.example.mysto.rickmortybuddyapp.Fragments.Locations.adapter;

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

import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.Locations;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.Result;
import com.example.mysto.rickmortybuddyapp.Location_Details_Activity;
import com.example.mysto.rickmortybuddyapp.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


        private Context mContext;
        private Locations mData;
        private List<Result> listLocations;

        public RecyclerViewAdapter(Context mContext, Locations mData) {
            this.mContext = mContext;
            this.mData = mData;
            this.listLocations = mData.getResults();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view;
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.lieux_fragment_item,parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

            holder.location_fragment_item_name.setText(listLocations.get(position).getName());
            holder.location_fragment_item_type.setText(listLocations.get(position).getType());
            holder.location_fragment_item_dimension.setText(listLocations.get(position).getDimension());

            //For Production
            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.downloader(new OkHttp3Downloader(mContext));
            builder.build()
                    .load(listLocations.get(position).getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.location_fragment_item__img);


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, Location_Details_Activity.class);
                    intent.putExtra("location_details", listLocations.get(position));
                    mContext.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mData.getResults().size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView location_fragment_item__img;
            TextView location_fragment_item_name;
            TextView location_fragment_item_type;
            TextView location_fragment_item_dimension;

            CardView cardView;


            public MyViewHolder(View itemView) {
                super(itemView);

                location_fragment_item__img = itemView.findViewById(R.id.location_fragment_item__img);
                location_fragment_item_name = itemView.findViewById(R.id.location_fragment_item_name);
                location_fragment_item_type = itemView.findViewById(R.id.location_fragment_item_type);
                location_fragment_item_dimension = itemView.findViewById(R.id.location_fragment_item_dimension);

                cardView = itemView.findViewById(R.id.cardview_fragment_item_id);

            }
        }
    }
