package uk.colessoft.android.hilllist.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.widget.TextView;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.DbHelper;

public class SplashScreenActivity extends AppCompatActivity {

	private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x00001;
	private Thread splashThread;
	private Handler handler;
	private ProgressDialog progressDialog;

	@Inject
	DbHelper dbAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		((BHApplication) getApplication()).getDbComponent().inject(this);

		setContentView(R.layout.splash);
		TextView link=(TextView) findViewById(R.id.TextView03);
		Linkify.addLinks(link, Linkify.ALL);

		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.arg1 == 0)
					progressDialog.incrementProgressBy(1);
				else {
					SplashScreenActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
					progressDialog.cancel();
				}
			}
		};

		progressDialog = new ProgressDialog(SplashScreenActivity.this);
		progressDialog.setTitle("Updating Database");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMax(20600);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();


		splashThread = new Thread() {

			@Override
			public void run() {


				SplashScreenActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);


				dbAdapter.touch(handler);
				handler.sendMessage(Message.obtain(handler, 0, 1, 1, 1));

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
