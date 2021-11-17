package com.example.go4lunch.ui.SettingsActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.databinding.ActivitySettingsBinding;
import com.example.go4lunch.domain.manager.UserManager;
import com.example.go4lunch.model.User;
import com.example.go4lunch.ui.SignInActivity.SignInActivity;

public class SettingsActivity extends AppCompatActivity {

    private final UserManager userManager = UserManager.getInstance();
    private final User currentUser = User.firebaseUserToUser(userManager.getCurrentUser());
    private ActivitySettingsBinding binding;
    private boolean notification = currentUser.isNotification();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.settingsName.setText(currentUser.getName());
        binding.settingsEmail.setText(currentUser.getMail());
        Glide.with(binding.settingsAvatar.getContext())
                .load(currentUser.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.settingsAvatar);
        binding.checkbox.setOnClickListener(v -> setNotification());
        //return t main activity
        binding.settingsLogout.setOnClickListener(v -> finish());
        binding.settingsNameButton.setOnClickListener(v -> userManager.updateUsername(binding.settingsName.getText().toString()));
        //delete user from firebase and logout
        binding.settingsDeletteBtn.setOnClickListener(v -> new AlertDialog.Builder(SettingsActivity.this)
                .setMessage("Sure?")
                .setPositiveButton("Yes", (dialogInterface, i) ->
                        userManager.deleteUser(SettingsActivity.this)
                                .addOnSuccessListener(aVoid -> {
                                    Intent intent = new Intent(this, SignInActivity.class);
                                    startActivity(intent);}
                                )
                )
                .setNegativeButton("No", null)
                .show());
    }

    public void settingCheckBox() {
        binding.checkbox.setChecked(notification);
    }

    //set if the user receive notification or not
    public void setNotification() {
        notification = !notification;
        userManager.updateNotification(notification).addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(),
                "Change saved", Toast.LENGTH_SHORT).show());
        Log.e("notification", "notification " + notification);
    }

    //check if the user receive notification or not
    @Override
    protected void onStart() {
        super.onStart();
        userManager.getUserData().addOnSuccessListener(user -> {
            notification = user.isNotification();
            settingCheckBox();
        });
    }
}