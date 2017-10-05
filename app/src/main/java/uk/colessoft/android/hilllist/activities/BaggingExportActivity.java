package uk.colessoft.android.hilllist.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.dialogs.FolderPicker;
import uk.colessoft.android.hilllist.database.DbHelper;

public class BaggingExportActivity extends AppCompatActivity implements OnClickListener,
        DialogInterface.OnClickListener {

    private FolderPicker mFolderDialog;
    private FolderPicker mFileDialog;
    private View mPickFolder;
    private View mPickFile;

    private static final int READ_REQUEST_CODE = 42;
    private static final int WRITE_REQUEST_CODE = 43;

    @Inject
    DbHelper dbAdapter;

    private String filePath;

    private static String folderPath = Environment
            .getExternalStorageDirectory().getAbsolutePath();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BHApplication) getApplication()).getDbComponent().inject(this);
        setContentView(R.layout.bagging_export);
        mPickFolder = findViewById(R.id.pick_folder);
        mPickFolder.setOnClickListener(this);
        mPickFile = findViewById(R.id.pick_file);
        View folderPathView = findViewById(R.id.folder_path);
        View filePathView = findViewById(R.id.file_path);

        mPickFile.setOnClickListener(this);

    }

    private void generateCsvFile(String sFileName) {

        Cursor baggedCursor = dbAdapter.getBaggedHillList();
        startManagingCursor(baggedCursor);
        try {
            File gpxfile = new File(folderPath, "bagging_export.csv");

            FileWriter writer = new FileWriter(gpxfile);

            writeCsvFromCursor(baggedCursor, writer);
            Toast backedUp = Toast.makeText(getApplication(), "Backed up to " + gpxfile.getAbsolutePath(),
                    Toast.LENGTH_SHORT);
            backedUp.show();
        } catch (IOException e) {
            Toast failed = Toast.makeText(getApplication(), "Backup Failed", Toast.LENGTH_LONG);
            failed.show();
        }

    }

    private void writeCsvFile(FileOutputStream stream) {

        Cursor baggedCursor = dbAdapter.getBaggedHillList();
        startManagingCursor(baggedCursor);
        try {
            Writer writer = new OutputStreamWriter(stream);

            writeCsvFromCursor(baggedCursor, writer);
            Toast backedUp = Toast.makeText(getApplication(), "Backed up data ",
                    Toast.LENGTH_SHORT);
            backedUp.show();
        } catch (IOException e) {
            Toast failed = Toast.makeText(getApplication(), "Backup Failed", Toast.LENGTH_LONG);
            failed.show();
        }

    }

    private void writeCsvFromCursor(Cursor baggedCursor, Writer writer) throws IOException {
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


        writer.flush();
        writer.close();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            try {
                ParcelFileDescriptor pfd = getContentResolver().
                        openFileDescriptor(uri, "w");

                FileOutputStream fileOutputStream =
                        new FileOutputStream(pfd.getFileDescriptor());

                writeCsvFile(fileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Toast backedUp = Toast.makeText(getApplication(), "Importing Data",
                    Toast.LENGTH_SHORT);
            backedUp.show();
            try {
                ParcelFileDescriptor pfd = getContentResolver().
                        openFileDescriptor(uri, "r");

                FileInputStream fileInputStream =
                        new FileInputStream(pfd.getFileDescriptor());

                dbAdapter.importBagging(new InputStreamReader(fileInputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void onClick(View v) {
        if (v == mPickFolder) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mFolderDialog = new FolderPicker(this, this, 0);
                mFolderDialog.show();
            } else {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

                intent.addCategory(Intent.CATEGORY_OPENABLE);

                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TITLE, "bagging_export.csv");
                startActivityForResult(intent, WRITE_REQUEST_CODE);
            }


        } else if (v == mPickFile) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mFileDialog = new FolderPicker(this, this, android.R.style.Theme,
                        true);
                mFileDialog.show();
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        }

    }

    private void importCsvFile() {

        try {
            dbAdapter.importBagging(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}