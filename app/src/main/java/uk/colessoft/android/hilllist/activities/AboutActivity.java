package uk.colessoft.android.hilllist.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import uk.colessoft.android.hilllist.R;

public class AboutActivity extends AppCompatActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		ImageButton donateButton=(ImageButton) findViewById(R.id.donateButton);
		TextView link = (TextView) findViewById(R.id.abt1);

		Linkify.addLinks(link, Linkify.ALL);
		
		donateButton.setOnClickListener(new View.OnClickListener(){

			private final String donateLink="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=QRHT3PRRVBTM4";

			public void onClick(View v) {
				Intent intent = new Intent("android.intent.action.VIEW",
						Uri.parse(donateLink));
				startActivity(intent);
				
			}});
		
	}

}
