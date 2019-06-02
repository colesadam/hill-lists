package uk.colessoft.android.hilllist.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;

public class BaggingExportActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private static final int WRITE_REQUEST_CODE = 43;

    @Inject
    BritishHillsDatasource dbAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BHApplication) getApplication()).getDbComponent().inject(this);
        setContentView(R.layout.bagging_export);
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


}