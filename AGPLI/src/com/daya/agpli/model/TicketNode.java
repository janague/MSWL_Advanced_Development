/*
 *
 * Copyright (C) Jose Alberto Navas Guerrero 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * Author : Jose Alberto Navas Guerrero <janague@gmail.com>
 *
 */

package com.daya.agpli.model;

import com.google.android.maps.GeoPoint;
import com.daya.agpli.R;

import android.os.Parcel;
import android.os.Parcelable;

public class TicketNode implements Parcelable {
	
	private enum Type {
	    unknown, software, workstation, printer, server, visit;
	}

	public static final Parcelable.Creator<TicketNode> CREATOR = new Parcelable.Creator<TicketNode>() {

		public TicketNode createFromParcel(Parcel in) {
			return new TicketNode(in);
		}

		public TicketNode[] newArray(int size) {
			return new TicketNode[size];
		}
	};

	public String mId;
	
	public String mTitle;
	public String mDescription;
	public String mType;

	public void setmType(String mType) {
		this.mType = mType;
	}

	public Double mLatitude;
	public Double mLongitude;
	
	public GeoPoint mGeoPoint;

	public TicketNode() {
	}

	public TicketNode(Parcel in) {
		mId = (String) in.readString();
		mTitle = (String) in.readString();
		mDescription = (String) in.readString();
		mType = (String) in.readString();

		// Read Double
		mLatitude = (Double) in.readDouble();
		mLongitude = (Double) in.readDouble();
		
		mGeoPoint = new GeoPoint((int)(mLatitude*1000000),(int)(mLongitude*1000000));

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		// Write String
		out.writeString(mId);
		out.writeString(mTitle);
		out.writeString(mDescription);
		out.writeString(mType);

		// Write Double
		out.writeDouble(mLatitude);
		out.writeDouble(mLongitude);
	}
	
	public String getmId() {
		return mId;
	}
	
	public void setmId(String mId) {
		this.mId = mId;
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

	public String getmType() {
		return mType;
	}

	public Double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(Double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public Double getmLongitude() {
		return mLongitude;
	}

	public void setmLongitude(Double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public GeoPoint getmGeoPoint() {
		return mGeoPoint;
	}

	public void setmGeoPoint(GeoPoint mGeoPoint) {
		this.mGeoPoint = mGeoPoint;
	}
	
	public int getRImageResource()
	{
		int imageResource = R.drawable.default_incident;
		
		Type type = Type.valueOf(mType);

		switch(type) {
		    case software:
		    	imageResource = R.drawable.software;
		        break;
		    case workstation:
		    	imageResource = R.drawable.workstation;
		        break;
		    case printer:
		    	imageResource = R.drawable.printer;
		        break;
		    case server:
		    	imageResource = R.drawable.server;
		        break;
		    case visit:
		    	imageResource = R.drawable.visit;
		        break;
		}
		
		return imageResource;
	}
	
	public int getRIconImageResource()
	{
		int imageResource = R.drawable.default_incident_icon;
		
		Type type = Type.valueOf(mType);

		switch(type) {
		    case software:
		    	imageResource = R.drawable.software_icon;
		        break;
		    case workstation:
		    	imageResource = R.drawable.workstation_icon;
		        break;
		    case printer:
		    	imageResource = R.drawable.printer_icon;
		        break;
		    case server:
		    	imageResource = R.drawable.server_icon;
		        break;
		    case visit:
		    	imageResource = R.drawable.visit_icon;
		        break;
		}
		
		return imageResource;
	}

}