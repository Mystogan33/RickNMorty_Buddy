package com.example.mysto.rickmortybuddyapp.Fragments.Locations.adapter;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.Location;
import com.example.mysto.rickmortybuddyapp.Fragments.Locations.models.RawLocationsServerResponse;
import com.example.mysto.rickmortybuddyapp.location_details.Location_Details_Activity;
import com.example.mysto.rickmortybuddyapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


        private Fragment parentFragment;
        private RawLocationsServerResponse mData;
        private List<Location> listLocations;

        public RecyclerViewAdapter(Fragment parentFragment, List<Location> mData) {
            this.parentFragment = parentFragment;
            this.listLocations = mData;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view;
            LayoutInflater mInflater = LayoutInflater.from(parentFragment.getActivity());
            view = mInflater.inflate(R.layout.lieux_fragment_item,parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            holder.location_fragment_item_name.setText(listLocations.get(position).getName());
            holder.location_fragment_item_type.setText(listLocations.get(position).getType());
            holder.location_fragment_item_dimension.setText(listLocations.get(position).getDimension());

            final ImageView imageView = holder.location_fragment_item__img;

            Picasso.with(parentFragment.getActivity())
                    .load(listLocations.get(position).getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.no_image)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(parentFragment.getActivity())
                                    .load(listLocations.get(position).getImage())
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

                    Intent intent = new Intent(parentFragment.getActivity(), Location_Details_Activity.class);
                    intent.putExtra("location_details", listLocations.get(position));

                    // Check if we're running on Android 5.0 or higher
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        Pair<View, String> p2 = Pair.create((View) holder.location_fragment_item__img, "imageLocation");

                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(parentFragment.getActivity(), p2);
                        parentFragment.startActivity(intent, optionsCompat.toBundle());

                    } else {
                        // Swap without transition
                    }
                }
            });

        }

        public void setFilter(List<Location> list) {

            listLocations = new ArrayList<>();
            listLocations.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return listLocations.size();
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
