package uk.colessoft.android.hilllist.mvp.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.colessoft.android.hilllist.contentprovider.HillsContentProvider;
import uk.colessoft.android.hilllist.database.ColumnKeys;
import uk.colessoft.android.hilllist.objects.Hill;

import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HEIGHTF;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HEIGHTM;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HILLNAME;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_ID;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_LATITUDE;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_LONGITUDE;
import static uk.colessoft.android.hilllist.database.TableNames.HILLS_TABLE;

public class HillsAsyncLoader extends AsyncTask<Void,Void,List<Hill>> {

    private Context mContext;

    public interface HillsLoaderListener {
        public void onSuccess(List<Hill> hills);

        public void onError(Exception e);
    }

    private boolean shouldFail;
    private HillsLoaderListener listener;

    public HillsAsyncLoader(boolean shouldFail, HillsLoaderListener listener, Context context) {
        this.listener = listener;
        this.shouldFail = shouldFail;
        this.mContext = context;
    }

    @Override
    protected List<Hill> doInBackground(Void... params) {

        Uri dataUri = HillsContentProvider.HILLS_CONTENT_URI;
        Cursor dataCursor = mContext.getContentResolver().query(dataUri,new String[]{KEY_HILLNAME,
                KEY_HEIGHTM, KEY_HEIGHTF,
                HILLS_TABLE + "." + KEY_ID,
                KEY_LATITUDE, KEY_LONGITUDE},null,null,null);


        Map<Integer,Hill> hillsMap = new HashMap<>();

        while(dataCursor.moveToNext()) {

            Hill hill = new Hill(dataCursor.getInt(dataCursor.getColumnIndexOrThrow(ColumnKeys.KEY_ID)),dataCursor.getString(dataCursor.getColumnIndex(ColumnKeys.KEY_HILLNAME)),dataCursor.getFloat(dataCursor.getColumnIndex(ColumnKeys.KEY_HEIGHTF)),dataCursor.getFloat(dataCursor.getColumnIndex(ColumnKeys.KEY_HEIGHTM)));
            hillsMap.put(hill.get_id(), hill);
        }
        dataCursor.close();
        return new ArrayList(hillsMap.values());

    }

    @Override protected void onPostExecute(List<Hill> hills) {

        if (isCancelled() || hills == null) {
            return;
        }

        if (shouldFail) {
            listener.onError(new Exception("Oops something went wrong"));
        } else {
            listener.onSuccess(hills);
        }
    }
}
