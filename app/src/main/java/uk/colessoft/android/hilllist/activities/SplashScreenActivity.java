package uk.colessoft.android.hilllist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import uk.colessoft.android.hilllist.R;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);
		TextView link=(TextView) findViewById(R.id.TextView03);
		Linkify.addLinks(link, Linkify.ALL);

		Thread splashThread = new Thread() {

			@Override
			public void run() {

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

		splashThread.start();

	}

}
