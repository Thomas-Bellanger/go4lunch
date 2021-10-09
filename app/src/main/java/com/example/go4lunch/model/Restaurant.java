package com.example.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.go4lunch.manager.RestaurantManager;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Parcelable {
    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
    public static Restaurant noRestaurant = new Restaurant("000", "none", "noAdress", "noType", 0, 0, "0", "none", "none", 0, 0, 0, 0);
    public static Restaurant restaurant1 = new Restaurant("1863", "restaurant 1", "8 Rue des restaurants", "French", 11, 21, "0.0", "https://i.pravatar.cc/150?u=a042581f4e29026704d", "https://i.pravatar.cc/150?u=a042581f4e29026704d", 3418, 10, 48.8675, 2.6901);
    public static Restaurant restaurant2 = new Restaurant("2854", "restaurant 2", "10 Rue des restaurants", "French", 11, 21,"0.0" , "https://i.pravatar.cc/150?u=a042581f4e29026704d", "https://i.pravatar.cc/150?u=a042581f4e29026704d", 3418, 5, 48.8341, 2.7958);

    private final RestaurantManager mRestaurantManager = RestaurantManager.getInstance();
    private String uid;
    private String name;
    private String adress;
    private List<User> joiners = new ArrayList<>();
    private String type;
    private int opening;
    private int closing;
    private String distance;
    private String avatar;
    private String url;
    private int phoneNumber;
    private boolean like;
    private int note;
    private double lat;
    private double lng;
    private LatLng mLatLng = new LatLng(getLat(), getLng());

    public Restaurant(String uid, String name, String adress, String type, int opening, int closing, String distance, String avatar, String url, int phoneNumber, int note, double lat, double lng) {
        this.uid = uid;
        this.name = name;
        this.adress = adress;
        this.joiners = new ArrayList<>();
        this.type = type;
        this.opening = opening;
        this.closing = closing;
        this.distance = distance;
        this.avatar = avatar;
        this.url = url;
        this.phoneNumber = phoneNumber;
        this.note = note;
        this.lat = lat;
        this.lng = lng;
    }

    public Restaurant() {
    }

    protected Restaurant(Parcel in) {
        uid = in.readString();
        name = in.readString();
        adress = in.readString();
        type = in.readString();
        opening = in.readInt();
        closing = in.readInt();
        distance = in.readString();
        avatar = in.readString();
        url = in.readString();
        phoneNumber = in.readInt();
        note = in.readInt();
        like = in.readByte() != 0;
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isLike() {
        return like;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<User> getJoiners() {
        return joiners;
    }

    public void setJoiners(List<com.example.go4lunch.model.User> joiners) {
        this.joiners = joiners;
    }

    public int getOpening() {
        return opening;
    }

    public void setOpening(int opening) {
        this.opening = opening;
    }

    public int getClosing() {
        return closing;
    }

    public void setClosing(int closing) {
        this.closing = closing;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(adress);
        dest.writeString(type);
        dest.writeInt(opening);
        dest.writeInt(closing);
        dest.writeString(distance);
        dest.writeString(avatar);
        dest.writeString(url);
        dest.writeInt(phoneNumber);
        dest.writeInt(note);
        dest.writeByte((byte) (like ? 1 : 0));
    }

    public void addJoiners(User user) {
        joiners.add(user);
    }

    public void removeJoiners(User user) {
        joiners.remove(user);
    }
}
