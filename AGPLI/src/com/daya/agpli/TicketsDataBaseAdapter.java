package com.daya.agpli;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.daya.agpli.model.Ticket;
import com.daya.agpli.model.TicketNode;
import com.daya.agpli.parser.JSONParser;
import com.daya.agpli.parser.PanoramioNode;
import com.daya.agpli.parser.TicketsJSONParser;
import com.google.android.maps.GeoPoint;

/**
 * @author janague
 * 
 */
public class TicketsDataBaseAdapter {
	private String TAG = getClass().getSimpleName();

	// Context of the application using the database.
	private final Context mContext;
	// url near ticket location server
	private String mUrlDataServer = "http://raw.github.com/janague/MSWL_Advanced_Development/master/AGPLIServer/data/";
	// private String
	// mUrlDataServer="http://raw.github.com/janague/MSWL_Advanced_Development/master/AGPLIServer/data/janagueTickets.json";

	// ArrayList
	private static ArrayList<TicketNode> mTicketsArray = new ArrayList<TicketNode>();

	public static ArrayList<TicketNode> getmTicketsArray() {
		return mTicketsArray;
	}

	public TicketsDataBaseAdapter(Context _context) {
		mContext = _context;
	}

	public TicketsDataBaseAdapter open() {
		clear();

		getTickets();

		return this;
	}

	public void close() {
		// TODO;
	}

	public void clear() {
		mTicketsArray.clear();
	}

	// Get tickets for fictitious server
	private void getTickets() {
		// Get current user
		String currentUser = ((AdvanceList) mContext).getmCurrentUser();
		Log.d(TAG, "currentUser: " + currentUser);
		TicketsJSONParser jparser = new TicketsJSONParser(mUrlDataServer
				+ currentUser + "Tickets.json");
		mTicketsArray = jparser.parser();

	}

}
