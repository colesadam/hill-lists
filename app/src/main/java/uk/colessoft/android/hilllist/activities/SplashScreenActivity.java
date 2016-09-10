package uk.colessoft.android.hilllist.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.util.Linkify;
import android.widget.TextView;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillsDatabaseHelper;

public class SplashScreenActivity extends FragmentActivity {

	private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x00001;
	private Thread splashThread;
	private Thread dbThread;



	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);
		TextView link=(TextView) findViewById(R.id.TextView03);
		Linkify.addLinks(link, Linkify.ALL);



		splashThread = new Thread() {

			@Override
			public void run() {
				HillsDatabaseHelper dbAdapter = HillsDatabaseHelper.getInstance(SplashScreenActivity.this);
				dbAdapter.touch();
				try {

					int waited = 0;

					while (waited < 1800) {

						sleep(100);

						waited += 100;

					}

				} catch (InterruptedException e) {

					// do nothing

				} finally {

					finish();
					Intent i = new Intent(SplashScreenActivity.this,

							Main.class);

					startActivity(i);

				}

			}

		};

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

				showMessageOKCancel("You need to allow access to the SD Card to read and write the Database",
						(dialog, which) -> ActivityCompat.requestPermissions(SplashScreenActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE));

			} else {

				// No explanation needed, we can request the permission.

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

			}
		}else {
			splashThread.start();

		}


	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					splashThread.start();


				} else {
					finish();
				}

			}

		}
	}

	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(SplashScreenActivity.this)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.create()
				.show();
	}

}
