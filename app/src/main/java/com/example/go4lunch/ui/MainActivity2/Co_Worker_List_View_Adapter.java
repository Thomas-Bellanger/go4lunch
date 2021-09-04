package com.example.go4lunch.ui.MainActivity2;

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
        holder.restaurant_Text.setText(user.getName() + " is eating " + user.getChosenRestaurant());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

