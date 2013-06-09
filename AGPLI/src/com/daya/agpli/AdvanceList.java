package com.daya.agpli;

import java.util.ArrayList;

import com.daya.agpli.R;
import com.daya.agpli.model.TicketNode;
import com.daya.agpli.service.LocService;
import com.google.android.maps.GeoPoint;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AdvanceList extends ListActivity {

	private String TAG = getClass().getSimpleName();
	public static Context mContext = null;

	private static final String LAST_LAT_POSITION = "LAST_LAT_POSITION";
	private static final String LAST_LON_POSITION = "LAST_LON_POSITION";

	private MyAdapter mAdapter = null;
	private TicketsDataBaseAdapter mTicketsDataBaseAdapter;

	private String mCurrentUser = null;

	/*
	 * // We define a structure to save the data public class Node { public
	 * String mTitle; public String mDescription; public Integer mImageResource;
	 * public Integer mFlagImage; public double mLatitude; public double
	 * mLongitude;
	 * 
	 * public Node() {} }
	 */

	public String getmCurrentUser() {
		return mCurrentUser;
	}

	// Incidents
	private static ArrayList<TicketNode> mArray = new ArrayList<TicketNode>();

	// Visits
	private static ArrayList<TicketNode> mVisits = new ArrayList<TicketNode>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incidents_view);

		mContext = this;

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitNetwork().build();
		StrictMode.setThreadPolicy(policy);

		// Localization service
		startService(new Intent(this, LocService.class));

		Intent i = getIntent();

		// set user login

		if (i != null) {
			String currentUser = i.getExtras().getString("USER");
			TextView tvUser = (TextView) this.findViewById(R.id.tvUser);

			Log.d(TAG, "currentUser: " + currentUser);
			if (currentUser != null) {
				tvUser.setText(currentUser);
				mCurrentUser = currentUser;
			} else {
				tvUser.setText("Non-defined");
				mCurrentUser = "Non-defined";
			}
		}
		initList();
		getData();

	}

	private void initList() {
		mAdapter = new MyAdapter(this);
		setListAdapter(mAdapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {

		if (position < mArray.size()) {
			TicketNode currentNode = mArray.get(position);

			Toast.makeText(this, currentNode.mTitle, Toast.LENGTH_SHORT).show();

			// Create a new intent to call other Activity.
			// Using the methods "putExtra" we can
			// send data to the new activity
			Intent intent = new Intent(AdvanceList.this, Map.class);
			intent.putExtra("ID", currentNode.mId);
			intent.putExtra("TITLE", currentNode.mTitle);
			intent.putExtra("TYPE", currentNode.mType);
			intent.putExtra("LATITUDE", currentNode.getmLatitude());
			intent.putExtra("LONGITUDE", currentNode.getmLongitude());
			intent.putExtra("USER", mCurrentUser);

			startActivity(intent);
		}
		if (position > (mArray.size() + 1)) {
			int i = position - (mArray.size());
			TicketNode currentNode = mVisits.get(i);

			Toast.makeText(this, currentNode.mTitle, Toast.LENGTH_SHORT).show();

			// Create a new intent to call other Activity.
			// Using the methods "putExtra" we can
			// send data to the new activity
			Intent intent = new Intent(AdvanceList.this, Map.class);
			intent.putExtra("ID", currentNode.mId);
			intent.putExtra("TITLE", currentNode.mTitle);
			intent.putExtra("TYPE", currentNode.mType);
			intent.putExtra("LATITUDE", currentNode.getmLatitude());
			intent.putExtra("LONGITUDE", currentNode.getmLongitude());
			intent.putExtra("USER", mCurrentUser);

			startActivity(intent);
		}

	}

	private void getData() {

		// get Incidents
		mTicketsDataBaseAdapter = new TicketsDataBaseAdapter(this);
		mTicketsDataBaseAdapter = mTicketsDataBaseAdapter.open();

		mArray = mTicketsDataBaseAdapter.getmTicketsArray();

		Log.d(TAG, "mArray size: " + mArray.size());

		// get Visits
		DownloadVisitsTask task = new DownloadVisitsTask();
		task.setAdapter(mAdapter);
		task.execute(null, null, null);

		// It is possible that it is empty yet
		mVisits = task.getmVisits();

	}

	public static class MyAdapter extends BaseAdapter {
		private String TAG = getClass().getSimpleName();
		private Context mContext;

		public MyAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {

			// size of incidents
			int size = mArray.size();

			// Title of visit
			size += 1;

			// size of visits.
			size += mVisits.size();

			Log.d(TAG, "getCount: " + size);
			return size;

		}

		@Override
		public Object getItem(int position) {
			return mArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = null;

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.ticket, null);

			int index = position;

			if (index < mArray.size()) {
				TextView tvId = (TextView) view.findViewById(R.id.id);
				tvId.setText(mArray.get(index).mId);

				TextView tvTitle = (TextView) view.findViewById(R.id.title);
				tvTitle.setText(mArray.get(index).mTitle);

				TextView tvDescription = (TextView) view
						.findViewById(R.id.description);
				tvDescription.setText(mArray.get(index).mDescription);

				ImageView img = (ImageView) view.findViewById(R.id.image);
				img.setImageDrawable(mContext.getResources().getDrawable(
						mArray.get(index).getRImageResource()));
			} else if (index > mArray.size()) {
				// Visits
				Log.d(TAG, "index: " + Integer.toString(index));
				int indexVisit = index - mArray.size();

				TextView tvId = (TextView) view.findViewById(R.id.id);
				tvId.setText(mVisits.get(indexVisit).mId);

				TextView tvTitle = (TextView) view.findViewById(R.id.title);
				tvTitle.setText(mVisits.get(indexVisit).mTitle);

				TextView tvDescription = (TextView) view
						.findViewById(R.id.description);
				tvDescription.setText(mVisits.get(indexVisit).mDescription);

				ImageView img = (ImageView) view.findViewById(R.id.image);
				img.setImageDrawable(mContext.getResources().getDrawable(
						R.drawable.visit));

			} else {
				// Title to separate tickets and visits.
				view = inflater.inflate(R.layout.visit_title, null);
			}

			return view;

		}
	}

}
