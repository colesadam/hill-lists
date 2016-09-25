package uk.colessoft.android.hilllist.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.utility.ResourceUtils;

public class AboutActivity extends AppCompatActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		ImageButton donateButton=(ImageButton) findViewById(R.id.donateButton);
		TextView link = (TextView) findViewById(R.id.abt1);

		Map<String,String> attributions= ResourceUtils.getHashMapResource(this,R.xml.cc_attributions);



		for(String key:attributions.keySet()){
			if(key.endsWith("attribution")) {

				TextView atv = new TextView(this);
				atv.setText(key.replace("_attribution"," ").replace("_"," ").toUpperCase()+attributions.get(key));
				TextView ltv = new TextView(this);
				ltv.setText(attributions.get(key.replace("attribution", "link")));
				ltv.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.about_padding));
				Linkify.addLinks(atv, Linkify.ALL);
				Linkify.addLinks(ltv, Linkify.ALL);
				((LinearLayout)findViewById(R.id.aboutLayout)).addView(atv);
				((LinearLayout)findViewById(R.id.aboutLayout)).addView(ltv);
			}
		}




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
