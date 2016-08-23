package uk.colessoft.android.hilllist.utility;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;

public class CheckboxCursorAdapter extends SimpleCursorAdapter {

	private Cursor c;
	private Context context;
	private ArrayList<String> checkList = new ArrayList<String>();
	
	public ArrayList<String> getCheckList() {
		return checkList;
	}



	public CheckboxCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.c = c;
		this.context = context;
	}
	
	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.check_hills, null);
		}
		this.c.moveToPosition(pos);		
		String hillName=c.getString(c.getColumnIndex(HillDbAdapter.KEY_HILLNAME));
		String dateClimbed=c.getString(c.getColumnIndex(HillDbAdapter.KEY_DATECLIMBED));
		String id=c.getString(c.getColumnIndex(HillDbAdapter.KEY_ID));
		
		CheckBox cBox = (CheckBox) v.findViewById(R.id.check_hill_climbed);
		cBox.setTag(id);
		cBox.setText(hillName);
		if (dateClimbed != null) {
			cBox.setChecked(true);	
			checkList.add((String) cBox.getTag());
		}else cBox.setChecked(false);
		cBox.setOnClickListener(new OnClickListener() {  
			//@Override
			public void onClick(View v) {
				CheckBox cBox = (CheckBox) v.findViewById(R.id.check_hill_climbed);
				if (cBox.isChecked()) {
					//cBox.setChecked(false);
					checkList.add((String) cBox.getTag());
				} 
				else if (!cBox.isChecked()) {
					//cBox.setChecked(true);
					checkList.remove(cBox.getTag());
				}
			}
		});

		return(v);
	}

}
