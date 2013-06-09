/*
 *
 * Copyright (C) Roberto Calvo Palomino
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
 * Author : Roberto Calvo Palomino <rocapal at gmail dot com>
 * Author : Jose Alberto Navas Guerrero <janague@gmail.com>
 */

package com.daya.agpli.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.daya.agpli.model.TicketNode;

import android.util.Log;

public class TicketsJSONParser {

	private final String TAG = getClass().getSimpleName();
	private String mUrl;

	public TicketsJSONParser(String url) {
		mUrl = url;
	}

	public ArrayList<TicketNode> parser() {

		String content = doGetPetition();

		return parseNodes(content);

	}

	private ArrayList<TicketNode> parseNodes(String content) {
		ArrayList<TicketNode> lnodes = new ArrayList<TicketNode>();

		JSONArray array;
		JSONObject json = null;

		try {
			json = new JSONObject(content);
			array = json.getJSONArray("results");

			for (int i = 0; i < array.length(); i++) {

				JSONObject node = array.getJSONObject(i);
				TicketNode pnode = parseNode(node);

				lnodes.add(pnode);
			}
			return lnodes;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
	}

	private TicketNode parseNode(JSONObject jsonData) {

		TicketNode pnode = new TicketNode();

		try {
			if (jsonData.has("id"))
				pnode.mId = jsonData.getString("id");
			
			if (jsonData.has("title"))
				pnode.mTitle = jsonData.getString("title");

			if (jsonData.has("description"))
				pnode.mDescription = jsonData.getString("description");

			if (jsonData.has("type"))
				pnode.mType = jsonData.getString("type");

			if (jsonData.has("position")) {
				JSONObject jsonPosition = jsonData.getJSONObject("position");
				if (jsonPosition.has("latitude"))
					pnode.mLatitude = jsonPosition.getDouble("latitude");

				if (jsonPosition.has("longitude"))
					pnode.mLongitude = jsonPosition.getDouble("longitude");
			}

			return pnode;
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			return null;
		}

	}

	private String doGetPetition() {

		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = null;

			httpGet = new HttpGet(mUrl);
			Log.d(TAG,"mURL: " + mUrl);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();

			String str = convertStreamToString(entity.getContent());

			return str;

		} catch (IOException e) {
			Log.e("doGet", e.getMessage());
			return null;
		}
	}

	private String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is),
				8 * 1024);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();

	}

}