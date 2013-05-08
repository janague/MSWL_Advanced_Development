package es.mswl.ad.exercies.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Activity3 extends Activity {

	private static int RETURN_VALUE = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_2_y_3);

		TextView tv = (TextView) this.findViewById(R.id.tvTitle);

		Intent i = getIntent();
		if (i != null) {
			String title = i.getStringExtra("TITLE");

			if (title != null) {
				tv.setText(title);
			}
		}

	}

	@Override
	public void finish() {
		Intent returnIntent = new Intent();

		returnIntent.putExtra(MainActivity.PARAM, Activity3.RETURN_VALUE);

		setResult(RESULT_OK, returnIntent);

		super.finish();
	}

}