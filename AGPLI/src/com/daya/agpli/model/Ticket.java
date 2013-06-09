package com.daya.agpli.model;

import com.google.android.maps.GeoPoint;

public class Ticket {
	public Ticket() {
		super();
		mGeoPoint = new GeoPoint(0,0);
	}

	public String mTitle;
	public String mDescription;
	public Integer mImageResource;
	public Integer mImageIconResource;
	public Integer mFlagImage;
		
	public GeoPoint mGeoPoint;

	public int getLatitude() {
		return mGeoPoint.getLatitudeE6();
	}

	public int getLongitude() {
		return mGeoPoint.getLongitudeE6();
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmDescription() {
		return mDescription;
	}

	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public Integer getmImageResource() {
		return mImageResource;
	}

	public void setmImageResource(Integer mImageResource) {
		this.mImageResource = mImageResource;
	}

	public Integer getmImageIconResource() {
		return mImageIconResource;
	}

	public void setmImageIconResource(Integer mImageIconResource) {
		this.mImageIconResource = mImageIconResource;
	}

	public Integer getmFlagImage() {
		return mFlagImage;
	}

	public void setmFlagImage(Integer mFlagImage) {
		this.mFlagImage = mFlagImage;
	}

	public GeoPoint getmGeoPoint() {
		return mGeoPoint;
	}

	public void setmGeoPoint(GeoPoint mGeoPoint) {
		this.mGeoPoint = mGeoPoint;
	}
}