package com.example.go4lunch.ui.SettingsActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivitySettingsBinding;
import com.example.go4lunch.manager.UserManager;

public class SettingsActivity extends AppCompatActivity {

    private final UserManager userManager = UserManager.getInstance();
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.settingsName.setText(userManager.getCurrentUser().getDisplayName());
        binding.settingsEmail.setText(userManager.getCurrentUser().getEmail());
        binding.settingsAvatar.setImageURI(userManager.getCurrentUser().getPhotoUrl());
        binding.settingsLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.signOut(SettingsActivity.this).addOnSuccessListener(Void -> finish());
            }
        });
        binding.settingsNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.updateUsername(binding.settingsName.getText().toString());
            }
        });
        binding.settingsDeletteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setMessage("Sure?")
                        .setPositiveButton("Yes", (dialogInterface, i) ->
                                userManager.deleteUser(SettingsActivity.this)
                                        .addOnSuccessListener(aVoid -> {
                                                    finish();
                                                }
                                        )
                        )
                        .setNegativeButton("No", null)
                        .show();


            }
        });

    }
}