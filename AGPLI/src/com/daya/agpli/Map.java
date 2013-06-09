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

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ScaleDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daya.agpli.R;
import com.daya.agpli.model.Ticket;
import com.daya.agpli.model.TicketNode;
import com.daya.agpli.service.LocService;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Map extends MapActivity {
	private String TAG = getClass().getSimpleName();

	private Location mCurrentLoc;
	private MapView mapview = null;
	private MapController mapControl = null;
	private TextView tvlocation;

	private TicketNode mNode = null;

	private static final int DIALOG_ADDRESS = 1;

	private static final int MENU_SETTINGS = Menu.FIRST + 1;
	private static final int MENU_ABOUT = Menu.FIRST + 2;
	private static final int MENU_ADDRESS = Menu.FIRST + 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mCurrentLoc = LocService.getLastLocation();

		mapview = (MapView) findViewById(R.id.mapView);

		tvlocation = (TextView) findViewById(R.id.mapTvlocation);

		mapview.setBuiltInZoomControls(true);
		mapControl = mapview.getController();

		Intent intent = getIntent();
		if (intent != null) {
			mNode = new TicketNode();

			mNode.mId = intent.getStringExtra("ID");
			TextView tvId = (TextView) this.findViewById(R.id.mapTvId);
			tvId.setText(mNode.mId);

			mNode.mTitle = intent.getStringExtra("TITLE");
			mNode.mType = intent.getStringExtra("TYPE");
			mNode.setmGeoPoint(new GeoPoint((int) (intent.getDoubleExtra(
					"LATITUDE", 0) * 1000000), (int) (intent.getDoubleExtra(
					"LONGITUDE", 0) * 1000000)));

			Log.d(TAG,
					"Latitude = "
							+ String.valueOf(mNode.mGeoPoint.getLatitudeE6()));

			// Get user information
			// - user name
			TextView tvUser = (TextView) this.findViewById(R.id.mapTvUser);

			String currentUser = intent.getExtras().getString("USER");

			Log.d(TAG, "currentUser: " + currentUser);
			if (currentUser != null) {
				tvUser.setText(currentUser);
			} else {
				tvUser.setText("Non-defined");
			}

		}

		drawIncident();
		refreshMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_SETTINGS, Menu.NONE, "Show Location")
				.setIcon(R.drawable.icon).setAlphabeticShortcut('S');

		menu.add(0, MENU_ADDRESS, Menu.NONE, "Address")
				.setIcon(R.drawable.icon).setAlphabeticShortcut('A');

		menu.add(0, MENU_ABOUT, Menu.NONE, "About").setIcon(R.drawable.icon)
				.setAlphabeticShortcut('B');

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case MENU_SETTINGS:
			refreshMap();
			break;

		case MENU_ADDRESS:
			showDialog(DIALOG_ADDRESS);
			break;

		case MENU_ABOUT:
			Toast.makeText(getBaseContext(), "About pulsado", Toast.LENGTH_LONG)
					.show();
			break;

		}

		return true;

	}

	private void drawIncident() {

		if (mNode == null) {
			Toast.makeText(getBaseContext(), "Incident not available!",
					Toast.LENGTH_LONG).show();

			return;
		}

		mapControl.setZoom(14);
		mapControl.animateTo(mNode.getmGeoPoint());

		MapOverlay myMapOver = new MapOverlay();
		myMapOver.setmText(mNode.mTitle);
		myMapOver.setDrawable(getResources().getDrawable(
				mNode.getRIconImageResource()));
		myMapOver.setGeoPoint(mNode.getmGeoPoint());

		final List<Overlay> overlays = mapview.getOverlays();
		overlays.add(myMapOver);

		mapview.setClickable(true);


	}

	private void refreshMap() {

		if (mCurrentLoc == null) {
			Toast.makeText(getBaseContext(), "Location not available!",
					Toast.LENGTH_LONG).show();

			return;
		}

		GeoPoint geoPoint = new GeoPoint(
				(int) (mCurrentLoc.getLatitude() * 1000000),
				(int) (mCurrentLoc.getLongitude() * 1000000));

		MapOverlay myMapOver = new MapOverlay();
		myMapOver.setmText("I'm here");
		myMapOver
				.setDrawable(getResources().getDrawable(R.drawable.drawingpin));
		myMapOver.setGeoPoint(geoPoint);

		final List<Overlay> overlays = mapview.getOverlays();

		overlays.add(myMapOver);

		mapview.setClickable(true);
		

		if (mCurrentLoc != null) {
			tvlocation.setText("Location: "
					+ String.valueOf(mCurrentLoc.getLatitude()).substring(0,6) + " , "
					+ String.valueOf(mCurrentLoc.getLongitude()).substring(0,6));
		}
		
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {

		case DIALOG_ADDRESS:

			LayoutInflater factory = (LayoutInflater) this
					.getApplicationContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);

			final View textEntryView = factory.inflate(R.layout.map_ticket,
					null);

			final EditText edit = (EditText) textEntryView
					.findViewById(R.id.txtmanual);
			edit.setText("");

			return new AlertDialog.Builder(this)
					.setTitle("Insert Address")
					.setView(textEntryView)
					.setPositiveButton("Search",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (!edit.getText().toString().equals(""))
										searchManual(edit.getText().toString());
									else
										Toast.makeText(getBaseContext(),
												"Empty Address",
												Toast.LENGTH_LONG).show();
								}
							})
					.setNegativeButton("cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							}).create();

		}
		return null;
	}

	public void searchManual(String address) {

		Geocoder gcod = new Geocoder(getApplicationContext());

		try {
			List<Address> addressList = gcod.getFromLocationName(address, 1);

			if (addressList.isEmpty()) {

				Toast.makeText(getBaseContext(), "There's no address",
						Toast.LENGTH_LONG).show();
			}
			if (addressList.get(0).hasLatitude()
					&& addressList.get(0).hasLongitude()) {

				Location loc = new Location("MANUAL_PROVIDER");
				loc.setLatitude(addressList.get(0).getLatitude());
				loc.setLongitude(addressList.get(0).getLongitude());

				mCurrentLoc = loc;
				refreshMap();

				// Send coordinates to the server
				Toast.makeText(getBaseContext(), "Location updated",
						Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(getBaseContext(), "There are no coordinates",
						Toast.LENGTH_LONG).show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
