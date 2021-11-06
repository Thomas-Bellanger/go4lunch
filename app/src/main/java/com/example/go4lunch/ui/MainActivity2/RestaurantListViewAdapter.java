package com.example.go4lunch.ui.MainActivity2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.DI.DI;
import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.service.ApiService;
import com.example.go4lunch.ui.RestaurantDetail.RestaurantDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListViewAdapter extends RecyclerView.Adapter<RestaurantListViewAdapter.ViewHolder> {

    private final List<Restaurant> restaurants;
    private Context mContext;
    private ApiService mApiService = DI.getASIService();
    private MapViewModel mMapViewModel = MapViewModel.getInstance();

    public RestaurantListViewAdapter(List<Restaurant> items) {
        restaurants = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent, false);
        mContext = view.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListViewAdapter.ViewHolder holder, int position) {

        Restaurant restaurant = restaurants.get(position);

        if (restaurant.getNote() > 50) {
            holder.stars.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow));
        }
        if (restaurant.getNote() > 100) {
            holder.stars2.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow));
        }
        if (restaurant.getNote() > 150) {
            holder.stars3.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow));
        }

        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantAdress.setText(restaurant.getAdress());
        holder.style.setText(restaurant.getType() + " - ");
        holder.distance.setText(restaurant.getDistance() + "m");
        Glide.with(holder.restaurantAvatar.getContext())
                .load(restaurant.getAvatar())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.restaurantAvatar);
        if(restaurant.getOpening() == true) {
            holder.hours.setText(R.string.Open);
            holder.hours.setTextColor(Color.GREEN);
        }
        else {
            holder.hours.setTextColor(Color.RED);
            holder.hours.setText(R.string.closed);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RestaurantDetail.class);
            intent.putExtra(RestaurantDetail.KEY_RESTAURANT, restaurant);
            v.getContext().startActivity(intent);
        });
        holder.number.setText("("+restaurant.getJoiners().size()+")");
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.restaurant_name)
        public TextView restaurantName;
        @BindView(R.id.restaurant_adress)
        public TextView restaurantAdress;
        @BindView(R.id.distance)
        public TextView distance;
        @BindView(R.id.restaurant_style)
        public TextView style;
        @BindView(R.id.number)
        public TextView number;
        @BindView(R.id.stars)
        public ImageView stars;
        @BindView(R.id.stars2)
        public ImageView stars2;
        @BindView(R.id.stars3)
        public ImageView stars3;
        @BindView(R.id.restaurant_avatar)
        public ImageView restaurantAvatar;
        @BindView(R.id.restaurant_hours)
        public TextView hours;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
