package es.mswl.ad.exercies.activities;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static String PARAM = "PARAM";
	public final static int F_ERROR = 616;

	private final int FROM_ACTIVITY2 = 2;
	private final int FROM_ACTIVITY3 = 3;

	private final static String URL = "http://docencia.etsit.urjc.es/moodle/course/view.php?id=129";
	private final static String PHONE_NUMBER = "444444444";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ----------------- Button activity 1 -------------------------
		Button bt1 = (Button) this.findViewById(R.id.button1);

		if (bt1 != null) {
			bt1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MainActivity.this,
							Activity1.class);
					startActivity(intent);
				}
			});
		}

		// ----------------- Button activity 2 -------------------------
		Button bt2 = (Button) this.findViewById(R.id.button2);

		if (bt2 != null) {
			bt2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MainActivity.this,
							Activity2.class);
					intent.putExtra("TITLE", "Activity 2");
					startActivityForResult(intent, FROM_ACTIVITY2);
				}
			});
		}

		// ----------------- Button activity 3 -------------------------
		Button bt3 = (Button) this.findViewById(R.id.button3);

		if (bt3 != null) {
			bt3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MainActivity.this,
							Activity3.class);
					intent.putExtra("TITLE", "Activity 3");
					startActivityForResult(intent, FROM_ACTIVITY3);
				}
			});
		}

		// ----------------- Open browser -------------------------
		Button bt_browser = (Button) this
				.findViewById(R.id.button_open_browser);

		if (bt_browser != null) {
			bt_browser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					String url = URL;
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			});
		}

		// ----------------- Call -------------------------
		Button bt_call = (Button) this
				.findViewById(R.id.button_call);

		if (bt_call != null) {
			bt_call.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + PHONE_NUMBER));
                    startActivity(intent);
				}
			});
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == MainActivity.RESULT_OK) {
			Integer param = data.getIntExtra(MainActivity.PARAM,
					MainActivity.F_ERROR);

			if (requestCode == FROM_ACTIVITY2) {

				Toast.makeText(this,
						"Come back from Activity 2: " + String.valueOf(param),
						Toast.LENGTH_LONG).show();
			} else if (requestCode == FROM_ACTIVITY3) {
				Toast.makeText(this,
						"Come back from Activity 3: " + String.valueOf(param),
						Toast.LENGTH_LONG).show();

			}
		}
	}

}
