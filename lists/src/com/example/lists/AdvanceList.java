package com.example.lists;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
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

	private MyAdapter mAdapter = null;

	private final static int PUBLICITY_POSITION = 3;

	// We define a structure to save the data
	public class Node {
		public String mTitle;
		public String mDescription;
		public Integer mImageResource;
		public Integer mFlagImage;
	}

	// ArrayList
	private static ArrayList<Node> mArray = new ArrayList<Node>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setData();

		mAdapter = new MyAdapter(this);
		setListAdapter(mAdapter);

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {

		// Create a new intent to call other Activity.
		// Using the methods "putExtra" we can
		// send data to the new activity

		// Calculate correct position

		int r = (position + 1) % PUBLICITY_POSITION;
		boolean isPublicity = r == 0;

		if (isPublicity) {
			Toast.makeText(this, "Publicity", Toast.LENGTH_SHORT).show();
		} else {
			int p = (PUBLICITY_POSITION - 1) * position / PUBLICITY_POSITION
					+ r - 1;

			Toast.makeText(this, mArray.get(p).mTitle, Toast.LENGTH_SHORT)
					.show();
		}

	}

	private void setData() {

		mArray.clear();

		Node mynode1 = new Node();

		// Restaurant 1

		mynode1.mTitle = getString(R.string.restaurant1);
		mynode1.mDescription = getString(R.string.restaurant1_description);
		mynode1.mImageResource = R.drawable.image1;
		mynode1.mFlagImage = R.drawable.flag_italian;

		mArray.add(mynode1);

		// Restaurant 2
		Node mynode2 = new Node();

		mynode2.mTitle = getString(R.string.restaurant2);
		mynode2.mDescription = getString(R.string.restaurant2_description);
		mynode2.mImageResource = R.drawable.image2;
		mynode1.mFlagImage = R.drawable.flag_japanese;

		mArray.add(mynode2);

		// Restaurant 3
		Node mynode3 = new Node();

		mynode3.mTitle = getString(R.string.restaurant3);
		mynode3.mDescription = getString(R.string.restaurant3_description);
		mynode3.mImageResource = R.drawable.image3;
		mynode3.mFlagImage = R.drawable.flag_gb;

		mArray.add(mynode3);

		mArray.addAll(mArray);

	}

	public static class MyAdapter extends BaseAdapter {
		private String TAG = getClass().getSimpleName();
		private Context mContext;

		public MyAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			Log.d(TAG,
					"number publicity = "
							+ String.valueOf(mArray.size() / PUBLICITY_POSITION));
			return mArray.size() + (mArray.size() / (PUBLICITY_POSITION - 1));

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

			if (((position + 1) % PUBLICITY_POSITION) == 0) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(R.layout.publicity, null);

				TextView tvTitle = (TextView) view.findViewById(R.id.publicity);
				tvTitle.setText(R.string.publicity);
				ImageView img = (ImageView) view
						.findViewById(R.id.imagePublicity);
				img.setImageDrawable(mContext.getResources().getDrawable(
						R.drawable.tuesdays_promo1));

			} else {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(R.layout.restaurant, null);

				int index = position - position / PUBLICITY_POSITION;

				TextView tvTitle = (TextView) view.findViewById(R.id.title);
				tvTitle.setText(mArray.get(index).mTitle);

				/*
				 * ImageView flagImg = (ImageView) view
				 * .findViewById(R.id.flag_image);
				 * flagImg.setImageDrawable(mContext.getResources().getDrawable(
				 * mArray.get(index).mFlagImage));
				 */

				TextView tvDescription = (TextView) view
						.findViewById(R.id.description);
				tvDescription.setText(mArray.get(index).mDescription);

				ImageView img = (ImageView) view.findViewById(R.id.image);
				img.setImageDrawable(mContext.getResources().getDrawable(
						mArray.get(index).mImageResource));
			}

			return view;

		}
	}
}
