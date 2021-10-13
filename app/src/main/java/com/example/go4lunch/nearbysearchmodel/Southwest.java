package com.example.go4lunch.nearbysearchmodel;

import com.google.gson.annotations.SerializedName;

public class Southwest{

	@SerializedName("lng")
	private double lng;

	@SerializedName("lat")
	private double lat;

	public double getLng(){
		return lng;
	}

	public double getLat(){
		return lat;
	}
}