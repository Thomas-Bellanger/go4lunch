package com.example.go4lunch.manager;

import android.content.Context;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

public class UserManager {

    private static volatile UserManager instance;
    private final UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public void createUser() {
        userRepository.createUser();
    }

    public Task<User> getUserData() {
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(User.class));
    }

    public Task<Void> updateUsername(String username) {
        return userRepository.updateUserName(username);
    }

    public Task<Void> updateLunch(Restaurant restaurant) {
        return userRepository.updateLunch(restaurant);
    }

    public Task<Void> deleteUser(Context context) {
        // Delete the user account from the Auth
        return userRepository.deleteUser(context).addOnCompleteListener(task -> {
            // Once done, delete the user datas from Firestore
            userRepository.deleteUserFromFirestore();
        });
    }

    public CollectionReference getUserCollection() {
        return userRepository.getUsersCollection();
    }

    public Task<Void> signOut(Context context) {
        return userRepository.signOut(context);
    }
}
