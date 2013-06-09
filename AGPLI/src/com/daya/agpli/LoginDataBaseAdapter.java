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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class LoginDataBaseAdapter {
	private String TAG = getClass().getSimpleName();

	static final String DATABASE_NAME = "aglpi.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;

	// SQL Statement to create a new database.
	static final String DATABASE_CREATE = "create table " + "LOGIN" + "( "
			+ "ID" + " integer primary key autoincrement,"
			+ "USERNAME  text,PASSWORD text); ";
	// Variable to hold the database instance
	public SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;

	public LoginDataBaseAdapter(Context _context) {
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public LoginDataBaseAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public void insertEntry(String userName, String password) {
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("USERNAME", userName);
		newValues.put("PASSWORD", password);

		// Insert the row into your table
		db.insert("LOGIN", null, newValues);
		Log.d(TAG, "Reminder Is Successfully Saved " + password);

	}

	public int deleteEntry(String UserName) {
		// String id=String.valueOf(ID);
		String where = "USERNAME=?";
		int numberOFEntriesDeleted = db.delete("LOGIN", where,
				new String[] { UserName });
		// Toast.makeText(context,
		// "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted,
		// Toast.LENGTH_LONG).show();
		return numberOFEntriesDeleted;
	}

	public String getSingleEntry(String userName) {
		Cursor cursor = db.query("LOGIN", null, " USERNAME=?",
				new String[] { userName }, null, null, null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			return "NOT EXIST";
		}
		cursor.moveToFirst();
		String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
		cursor.close();
		return password;
	}

	public void updateEntry(String userName, String password) {
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("USERNAME", userName);
		updatedValues.put("PASSWORD", password);

		String where = "USERNAME = ?";
		db.update("LOGIN", updatedValues, where, new String[] { userName });
	}
}
