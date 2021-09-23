package com.example.go4lunch.ui.SettingsActivity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.databinding.ActivitySettingsBinding;
import com.example.go4lunch.manager.UserManager;
import com.example.go4lunch.model.User;

public class SettingsActivity extends AppCompatActivity {

    private final UserManager userManager = UserManager.getInstance();
    private final User currentUser = User.firebaseUserToUser(userManager.getCurrentUser());
    private ActivitySettingsBinding binding;

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
        binding.settingsLogout.setOnClickListener(v -> finish());
        binding.settingsNameButton.setOnClickListener(v -> userManager.updateUsername(binding.settingsName.getText().toString()));
        binding.settingsDeletteBtn.setOnClickListener(v -> new AlertDialog.Builder(SettingsActivity.this)
                .setMessage("Sure?")
                .setPositiveButton("Yes", (dialogInterface, i) ->
                        userManager.deleteUser(SettingsActivity.this)
                                .addOnSuccessListener(aVoid -> finish()
                                )
                )
                .setNegativeButton("No", null)
                .show());
    }
}