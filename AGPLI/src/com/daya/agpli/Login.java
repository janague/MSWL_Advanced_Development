/*
 *
 *  Copyright (C) Jose Alberto Navas Guerrero
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/. 
 *
 *  Author : Jose Alberto Navas Guerrero <janague@gmail.com>
 *
 */

package com.daya.agpli;

import com.daya.agpli.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private String TAG = getClass().getSimpleName();

	private static final String CAGPLI_PREFS = "AGPLIPreferences";


	private static final String DEFAULT_USER1 = "janague";
	private static final String DEFAULT_USER2 = "rocapal";
	private static final String DEFAULT_PASSWORD = "12345";

	protected static final String PREF_LAST_USER = "PREF_LAST_USER";


	SharedPreferences mSharedPreferences;

	private LoginDataBaseAdapter mLoginDataBaseAdapter;

	private EditText txtUserName;
	private EditText txtPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// get preferences
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		// get Instance of Database Adapter
		mLoginDataBaseAdapter = new LoginDataBaseAdapter(this);
		mLoginDataBaseAdapter = mLoginDataBaseAdapter.open();

		insertDefaultUsers();

		addListenerOnCancelButton();
		addListenerOnLoginButton();

		txtUserName = (EditText) findViewById(R.id.loginUname);
		txtPassword = (EditText) findViewById(R.id.loginPwd);

		String lastUser = loadPreference(PREF_LAST_USER);
		if (lastUser == null) {
			txtUserName.requestFocus();
		} else {
			txtUserName.setText(lastUser);
			txtPassword.requestFocus();
		}
	}

	private void addListenerOnLoginButton() {

		Button button = (Button) findViewById(R.id.loginBtnLogin);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				txtUserName = (EditText) findViewById(R.id.loginUname);
				txtPassword = (EditText) findViewById(R.id.loginPwd);

				Log.i(TAG, "User name: " + txtUserName.getText());
				Log.i(TAG, "Password: " + txtPassword.getText());

				String userName = txtUserName.getText().toString();
				String password = txtPassword.getText().toString();
				// fetch the Password form database for respective user name
				String storedPassword = mLoginDataBaseAdapter
						.getSingleEntry(userName);
				Log.i(TAG, "storedPassword: " + storedPassword);

				if (password.isEmpty()) {
					Toast.makeText(getApplicationContext(), "Field Vaccant",
							Toast.LENGTH_SHORT).show();
				} else {
					if (password.equals(storedPassword)) {
						Toast.makeText(getApplicationContext(),
								"Congrats: Login Successfull",
								Toast.LENGTH_SHORT).show();

						// Save last login user
						savePreference(PREF_LAST_USER, userName);

						Intent intent = new Intent(Login.this,
								AdvanceList.class);
						intent.putExtra("USER", txtUserName.getText().toString());
						startActivity(intent);
					} else {
						Toast.makeText(getApplicationContext(),
								"Password does not match", Toast.LENGTH_SHORT)
								.show();
						txtPassword.setText("");
						txtPassword.requestFocus();
					}

				}
			}

		});

	}

	private void addListenerOnCancelButton() {
		Button button = (Button) findViewById(R.id.loginBtnCancel);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				// startActivity(getIntent());
			}

		});

	}

	private void savePreference(String key, String value) {
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private String loadPreference(String key) {
		return mSharedPreferences.getString(key, null);
	}

	private void insertDefaultUsers() {
		// fetch the password form database for respective user name
		String storedPassword = mLoginDataBaseAdapter
				.getSingleEntry(DEFAULT_USER1);
		Log.i(TAG, "insertDefaultUsers - storedPassword: " + storedPassword);
		if (storedPassword.equals("NOT EXIST")) {
			mLoginDataBaseAdapter.insertEntry(DEFAULT_USER1, DEFAULT_PASSWORD);
		}
		storedPassword = mLoginDataBaseAdapter.getSingleEntry(DEFAULT_USER2);
		Log.i(TAG, "insertDefaultUsers - storedPassword: " + storedPassword);
		if (storedPassword.equals("NOT EXIST")) {
			mLoginDataBaseAdapter.insertEntry(DEFAULT_USER2, DEFAULT_PASSWORD);
		}
	}
}
