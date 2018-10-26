package com.example.mysto.rickmortybuddyapp.Fragments.Characters.adapter;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mysto.rickmortybuddyapp.Fragments.Characters.models.Character;
import com.example.mysto.rickmortybuddyapp.character_details.Personnage_Details_Activity;
import com.example.mysto.rickmortybuddyapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private Fragment mContext;
    private List<Character> listCharacters;

    public RecyclerViewAdapter(Fragment mContext, List<Character> mData) {
        this.mContext = mContext;
        this.listCharacters = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext.getContext());
        return new MyViewHolder(mInflater.inflate(R.layout.characters_fragment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.personnage_fragment_item_name.setText(listCharacters.get(position).getName());

        String status = listCharacters.get(position).getStatus();
        if(status.toLowerCase().equals("dead")) {
            holder.personnage_fragment_item_status.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.colorDanger));
            holder.personnage_fragment_item_status.setText(mContext.getResources().getString(R.string.status_dead));
        } else if(status.toLowerCase().equals("alive")) {
            holder.personnage_fragment_item_status.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.colorValidate));
            holder.personnage_fragment_item_status.setText(mContext.getResources().getString(R.string.status_alive));
        } else {
            holder.personnage_fragment_item_status.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.followersBg));
            holder.personnage_fragment_item_status.setText(mContext.getResources().getString(R.string.status_unknown));
        }

        holder.personnage_fragment_item_species.setText(listCharacters.get(position).getSpecies());

        String gender = listCharacters.get(position).getGender();

        if(gender.toLowerCase().equals("female")) {
            holder.personnage_fragment_item_gender.setText(mContext.getResources().getString(R.string.gender_female));
        } else if(gender.toLowerCase().equals("male")) {
            holder.personnage_fragment_item_gender.setText(mContext.getResources().getString(R.string.gender_male));
        } else {
            holder.personnage_fragment_item_gender.setText(gender);
        }

        holder.personnage_fragment_item_origin.setText(listCharacters.get(position).getOrigin().getName());
        holder.personnage_fragment_item_last_location.setText(listCharacters.get(position).getLocation().getName());

        final ImageView imageView = holder.personnage_fragment_item__img;

        Picasso.with(mContext.getActivity())
                .load(listCharacters.get(position).getImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.no_data)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() { }
                    @Override
                    public void onError() {
                        Picasso.with(mContext.getActivity())
                                .load(listCharacters.get(position).getImage())
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
                Intent intent = new Intent(mContext.getActivity(), Personnage_Details_Activity.class);
                intent.putExtra("personnage_details", listCharacters.get(position));

                // Check if we're running on Android 5.0 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext.getActivity()).toBundle());
                } else {
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listCharacters.size();
    }

    public void setFilter(List<Character> list) {

        listCharacters = new ArrayList<>();
        listCharacters.addAll(list);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.personnage_fragment_item__img)
        ImageView personnage_fragment_item__img;
        @BindView(R.id.personnage_fragment_item_name)
        TextView personnage_fragment_item_name;
        @BindView(R.id.personnage_fragment_item_status)
        TextView personnage_fragment_item_status;
        @BindView(R.id.personnage_fragment_item_species)
        TextView personnage_fragment_item_species;
        @BindView(R.id.personnage_fragment_item_gender)
        TextView personnage_fragment_item_gender;
        @BindView(R.id.personnage_fragment_item_origin)
        TextView personnage_fragment_item_origin;
        @BindView(R.id.personnage_fragment_item_last_location)
        TextView personnage_fragment_item_last_location;
        @BindView(R.id.cardview_fragment_item_id)
        CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
