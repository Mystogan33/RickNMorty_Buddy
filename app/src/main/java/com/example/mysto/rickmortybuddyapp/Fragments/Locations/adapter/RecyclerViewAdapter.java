package com.example.mysto.rickmortybuddyapp.Fragments.Locations.adapter;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.core.util.Pair;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

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

        private Fragment mContext;
        private List<Location> listLocations;

        public RecyclerViewAdapter(Fragment mContext, List<Location> mData) {
            this.mContext = mContext;
            this.listLocations = mData;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(mContext.getActivity());
            return new MyViewHolder(mInflater.inflate(R.layout.locations_fragment_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            holder.location_fragment_item_name.setText(listLocations.get(position).getName());
            holder.location_fragment_item_type.setText(listLocations.get(position).getType());
            holder.location_fragment_item_dimension.setText(listLocations.get(position).getDimension());

            final ImageView imageView = holder.location_fragment_item__img;

            Picasso.with(mContext.getActivity())
                    .load(listLocations.get(position).getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.no_image)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() { }
                        @Override
                        public void onError() {
                            Picasso.with(mContext.getActivity())
                                    .load(listLocations.get(position).getImage())
                                    .placeholder((R.drawable.ic_launcher_background))
                                    .error(R.drawable.no_image)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() { }
                                        @Override
                                        public void onError() { }
                                    });
                        }
                    });

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext.getActivity(), Location_Details_Activity.class);
                    intent.putExtra("location_details", listLocations.get(position));

                    // Check if we're running on Android 5.0 or higher
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Pair<View, String> p2 = Pair.create((View) holder.location_fragment_item__img, "imageLocation");
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext.getActivity(), p2);
                        mContext.startActivity(intent, optionsCompat.toBundle());
                    } else {
                        mContext.startActivity(intent);
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

            @BindView(R.id.location_fragment_item__img)
            ImageView location_fragment_item__img;
            @BindView(R.id.location_fragment_item_name)
            TextView location_fragment_item_name;
            @BindView(R.id.location_fragment_item_type)
            TextView location_fragment_item_type;
            @BindView(R.id.location_fragment_item_dimension)
            TextView location_fragment_item_dimension;
            @BindView(R.id.cardview_fragment_item_id)
            CardView cardView;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
