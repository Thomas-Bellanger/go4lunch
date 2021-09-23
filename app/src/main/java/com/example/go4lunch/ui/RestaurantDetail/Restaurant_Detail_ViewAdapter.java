package com.example.go4lunch.ui.RestaurantDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Restaurant_Detail_ViewAdapter extends RecyclerView.Adapter<Restaurant_Detail_ViewAdapter.ViewHolder> {
    private List<User> joiners;

    public Restaurant_Detail_ViewAdapter(List<User> items) {
        joiners = items;
    }


    @Override
    public Restaurant_Detail_ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workmates_join, parent, false);
        return new Restaurant_Detail_ViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Restaurant_Detail_ViewAdapter.ViewHolder holder, int position) {
        User joiner = joiners.get(position);


        holder.name.setText(joiner.getName());
        Glide.with(holder.avatar.getContext())
                .load(joiner.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return joiners.size();
    }

    public void update(List<User> newJoiners) {
        joiners = newJoiners;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.joiner_name)
        TextView name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
