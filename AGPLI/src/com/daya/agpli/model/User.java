package com.daya.agpli.model;

public class User {
	private String TAG = getClass().getSimpleName();
	
	private int mId;
	private String mName;
	private String mPassword;
	
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmPassword() {
		return mPassword;
	}
	public void setmPassword(String mPassword) {
		this.mPassword = mPassword;
	}

}
