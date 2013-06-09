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

import java.util.ArrayList;

import com.daya.agpli.AdvanceList.MyAdapter;
import com.daya.agpli.model.TicketNode;
import com.daya.agpli.parser.JSONParser;
import com.daya.agpli.parser.PanoramioNode;
import com.daya.agpli.service.LocService;
import com.google.android.maps.GeoPoint;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadVisitsTask extends AsyncTask<Void, Void, Void> {
	private static final int MAX_VISITS = 10;
	private static final String VISIT = "visit";

	private String TAG = getClass().getSimpleName();
	
	private MyAdapter mAdapter = null;

	// visit near current location, default url
	private String mUrlPanoramioServer = "http://rest.libregeosocial.org/social/layer/560/search/?search=&latitude=40.31329&longitude=-3.83886&radius=1.0&category=0&elems=30&page=1&format=JSON";

	ArrayList<PanoramioNode> mList;
	ArrayList<TicketNode> mVisits = new ArrayList<TicketNode>();

	protected void onPreExecute() {
		Log.d(TAG, "Starting  ...");
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// No interact with the UI in this method
		try {
			Location currentLocation = LocService.getLastLocation();
			while (currentLocation == null) {
				currentLocation = LocService.getLastLocation();
				Thread.sleep(3000);
			}
			if (currentLocation != null) {
				Log.d(TAG, "Url current location");
				mUrlPanoramioServer = "http://rest.libregeosocial.org/social/layer/560/search/?search=&latitude="
						+ String.valueOf(currentLocation.getLatitude())
						+ "&longitude="
						+ String.valueOf(currentLocation.getLongitude())
						+ "&radius=1.0&category=0&elems=30&page=1&format=JSON";
			}

			Log.d(TAG, "Downloading ...");

			JSONParser jparser = new JSONParser(mUrlPanoramioServer);
			mList = jparser.parser();
			Log.d(TAG, "Number of elements: " + mList.size());

			Thread.sleep(10000);

			Log.d(TAG, "Download completed");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void unused) {
		if (mList != null)
		{
			int i = 0;
			while (i < mList.size() && i < MAX_VISITS)
			{
				PanoramioNode node = mList.get(i);
				
				TicketNode visitAsTicket = new TicketNode();
				visitAsTicket.setmId(Integer.toString(i+1));
				visitAsTicket.setmDescription("URL:" + node.info_url);
				visitAsTicket.setmTitle(node.name);
				visitAsTicket.setmType(VISIT);
				
				GeoPoint geoPoint = new GeoPoint(
						(int) (node.latitude * 1000000),
						(int) (node.longitude * 1000000));
				visitAsTicket.setmGeoPoint(geoPoint);
				visitAsTicket.setmLatitude(node.latitude);
				visitAsTicket.setmLongitude(node.longitude);
				
				
				mVisits.add(visitAsTicket);
				i++;
			}
			Log.d(TAG, "Number of visits: " + mVisits.size());
		
		}
		Log.d(TAG, "Finished.");
		mAdapter.notifyDataSetChanged();
	}

	public ArrayList<TicketNode> getmVisits() {

		return mVisits;
	}

	public void setAdapter(MyAdapter adapter) {
		mAdapter = adapter;		
	}

}