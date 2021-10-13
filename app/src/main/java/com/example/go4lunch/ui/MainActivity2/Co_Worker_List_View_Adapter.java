package com.example.go4lunch.ui.MainActivity2;

import android.content.Intent;
import android.graphics.Color;
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
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.ui.RestaurantDetail.RestaurantDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Co_Worker_List_View_Adapter extends RecyclerView.Adapter<Co_Worker_List_View_Adapter.ViewHolder> {
    private final List<User> mUsers;


    public Co_Worker_List_View_Adapter(List<User> items) {
        mUsers = items;
    }

    @Override
    public Co_Worker_List_View_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workmates_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        String []name =user.getName().split(" ");
        holder.name.setText(name[0]);
        if (user.getChosenRestaurant() == null) {
            holder.eating.setText(" ");
            holder.restaurant_Text.setText(R.string.notDecided);
            holder.name.setTextColor(Color.GRAY);
            holder.restaurant_Text.setTextColor(Color.GRAY);
        }
        else {
            holder.restaurant_Text.setText(user.getChosenRestaurant().getType()+"("+ user.getChosenRestaurant().getName()+")");
            holder.restaurant_Text.setTextColor(Color.BLACK);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), RestaurantDetail.class);
                intent.putExtra(RestaurantDetail.KEY_RESTAURANT, user.getChosenRestaurant());
                v.getContext().startActivity(intent);
            });
        }
        Glide.with(holder.co_Worker_Avatar.getContext())
                .load(user.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.co_Worker_Avatar);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        public ImageView co_Worker_Avatar;
        @BindView(R.id.restaurant_text)
        public TextView restaurant_Text;
        @BindView(R.id.coWorkerName)
        public TextView name;
        @BindView(R.id.isEating)
        public TextView eating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

