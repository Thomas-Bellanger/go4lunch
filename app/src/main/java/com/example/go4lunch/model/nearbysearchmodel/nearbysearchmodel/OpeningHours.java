package com.example.go4lunch.model.nearbysearchmodel.nearbysearchmodel;

import com.example.go4lunch.model.detailmodel.PeriodsItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours{

	@SerializedName("open_now")
	private boolean openNow;

	@SerializedName("periods")
	private List<PeriodsItem> periods;

	@SerializedName("weekday_text")
	private List<String> weekdayText;

	public boolean isOpenNow(){
		return openNow;
	}

	public List<PeriodsItem> getPeriods(){
		return periods;
	}

	public List<String> getWeekdayText(){
		return weekdayText;
	}
}