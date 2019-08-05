package uk.colessoft.android.hilllist.utility;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillsTables;
import uk.colessoft.android.hilllist.domain.entity.Bagging;

class CheckboxCursorAdapter extends SimpleCursorAdapter {

	private final Cursor c;
	private final Context context;
	private final ArrayList<String> checkList = new ArrayList<>();
	
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
		String hillName=c.getString(c.getColumnIndex(HillsTables.KEY_HILLNAME));
		String dateClimbed=c.getString(c.getColumnIndex(Bagging.KEY_DATECLIMBED));
		String id=c.getString(c.getColumnIndex(HillsTables.KEY_ID));
		
		CheckBox cBox = (CheckBox) v.findViewById(R.id.check_hill_climbed);
		cBox.setTag(id);
		cBox.setText(hillName);
		if (dateClimbed != null) {
			cBox.setChecked(true);	
			checkList.add((String) cBox.getTag());
		}else cBox.setChecked(false);
		cBox.setOnClickListener(v1 -> {
            CheckBox cBox1 = (CheckBox) v1.findViewById(R.id.check_hill_climbed);
            if (cBox1.isChecked()) {
                //cBox.setChecked(false);
                checkList.add((String) cBox1.getTag());
            }
            else if (!cBox1.isChecked()) {
                //cBox.setChecked(true);
                checkList.remove(cBox1.getTag());
            }
        });

		return(v);
	}

}
