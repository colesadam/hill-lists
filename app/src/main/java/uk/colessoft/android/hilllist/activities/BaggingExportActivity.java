package uk.colessoft.android.hilllist.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.dialogs.FolderPicker;
import uk.colessoft.android.hilllist.database.HillDbAdapter;

public class BaggingExportActivity extends Activity implements OnClickListener,
		DialogInterface.OnClickListener {

	private FolderPicker mFolderDialog;
	private FolderPicker mFileDialog;
	private View mPickFolder;
	private View mPickFile;
	private View folderPathView;

	private View filePathView;

	private String filePath;

	private static String folderPath = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bagging_export);
		mPickFolder = findViewById(R.id.pick_folder);
		mPickFolder.setOnClickListener(this);
		mPickFile = findViewById(R.id.pick_file);
		folderPathView = findViewById(R.id.folder_path);
		filePathView = findViewById(R.id.file_path);

		mPickFile.setOnClickListener(this);

	}

	private void generateCsvFile(String sFileName) {
		HillDbAdapter dbAdapter = new HillDbAdapter(this);
		dbAdapter.open();
		Cursor baggedCursor = dbAdapter.getBaggedHillList();
		startManagingCursor(baggedCursor);
		try {
			File gpxfile = new File(folderPath, "bagging_export.csv");

			FileWriter writer = new FileWriter(gpxfile);

			if (baggedCursor.moveToFirst()) {
				// Iterate over each cursor.
				do {

					writer.append("'");
					writer.append(baggedCursor.getString(0));
					writer.append("'");
					writer.append(',');
					writer.append("'");
					writer.append(baggedCursor.getString(2));
					writer.append("'");
					writer.append(',');
					writer.append("'");
					writer.append(baggedCursor.getString(3));
					writer.append("'");
					writer.append('\n');

				} while (baggedCursor.moveToNext());
			}

			// generate whatever data you want

			writer.flush();
			writer.close();
			Toast backedUp= Toast.makeText(getApplication(), "Backed up to "+gpxfile.getAbsolutePath(),
					Toast.LENGTH_SHORT);
			backedUp.show();
		} catch (IOException e) {
			Toast failed=Toast.makeText(getApplication(), "Backup Failed", Toast.LENGTH_LONG);
			failed.show();
		}
		dbAdapter.close();

	}

	public void onClick(DialogInterface dialog, int which) {
		if (dialog == mFolderDialog) {
			((TextView) findViewById(R.id.folder_path)).setText(mFolderDialog
					.getPath());
			folderPath = mFolderDialog.getPath();
			generateCsvFile("bagging.csv");
		}
		if (dialog == mFileDialog) {
			String path = mFileDialog.getPath();
			if (path == null) {
				path = "no file selected";
			}
			((TextView) findViewById(R.id.file_path)).setText(path);
			filePath = path;
			importCsvFile();
		}
	}

	public void onClick(View v) {
		if (v == mPickFolder) {
			mFolderDialog = new FolderPicker(this, this, 0);
			
			mFolderDialog.show();
		} else if (v == mPickFile) {
			mFileDialog = new FolderPicker(this, this, android.R.style.Theme,
					true);
			
			mFileDialog.show();
		
		}

	}

	private void importCsvFile() {
		HillDbAdapter dbAdapter = new HillDbAdapter(this);
		dbAdapter.open();
		dbAdapter.importBagging(filePath);

	}
}