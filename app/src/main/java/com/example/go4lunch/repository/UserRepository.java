package com.example.go4lunch.repository;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.go4lunch.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public final class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static volatile UserRepository instance;
    private final String CHOSEN_LUNCH = "lunch";

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    public String getCurrentUserUid() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context) {
        return AuthUI.getInstance().delete(context);
    }

    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();

            User userToCreate = new User(uid, username, urlPicture);
            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his data (isMentor)
            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(CHOSEN_LUNCH)) {
                    userToCreate.setChosenRestaurant((String) documentSnapshot.get(CHOSEN_LUNCH));

                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }
    //get userdata from firestore
    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUid();
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }

    //update the name of the user
    public Task<Void> updateUserName(String username) {
        String uid = this.getCurrentUserUid();
        if (uid != null) {
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        } else {
            return null;
        }
    }

    //update the lunch of the user
    public void updateLunch(String lunch) {
        String uid = this.getCurrentUserUid();
        if (uid != null) {
            this.getUsersCollection().document(uid).update(CHOSEN_LUNCH, lunch);
        }
    }

    //delete user from firestore
    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUid();
        if (uid != null) {
            this.getUsersCollection().document(uid).delete();
        }
    }

    public List<User> users = new ArrayList<>();

    public List<User> getAllUsers(){
        return users;
    }

}

