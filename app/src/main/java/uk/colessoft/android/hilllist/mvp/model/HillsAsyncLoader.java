package uk.colessoft.android.hilllist.mvp.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.colessoft.android.hilllist.activities.Main;
import uk.colessoft.android.hilllist.contentprovider.HillsContentProvider;
import uk.colessoft.android.hilllist.database.ColumnKeys;
import uk.colessoft.android.hilllist.database.TableNames;
import uk.colessoft.android.hilllist.objects.Hill;

import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HEIGHTF;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HEIGHTM;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HILLNAME;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_ID;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_LATITUDE;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_LONGITUDE;
import static uk.colessoft.android.hilllist.database.TableNames.HILLS_TABLE;

public class HillsAsyncLoader extends AsyncTask<Bundle,Void,List<Hill>> {

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
    protected List<Hill> doInBackground(Bundle... params) {

        String hilltype = params[0].getString("hilltype");
        String country = params[0].getString("country");
        String where = params[0].getString("search", "");
        String countryClause="";
        switch (country) {
            case Main.SCOTLAND: {
                countryClause = "cast(_Section as float) between 1 and 28.9";
                break;

            }
            case Main.WALES: {
                countryClause = "cast(_Section as float) between 30 and 32.9";
                break;

            }
            case Main.ENGLAND: {
                countryClause = "cast(_Section as float) between 33 and 42.9";
                break;

            }
            case Main.OTHER_GB: {
                countryClause = "_Section='29' OR cast(_Section as float)>42.9";
                break;

            }

        }

        //String[] selectionArgs ={};
        ArrayList<String> selectionArgs = new ArrayList<String>();

//
//        if (filter == 1)
//            where = KEY_DATECLIMBED + " NOT NULL";
//        else if (filter == 2)
//            where = KEY_DATECLIMBED + " IS NULL";
//        else

        if (hilltype != null){
            if (!"".equals(where))
                where = where + " AND ";
            where = where + "(" + TableNames.HILLTYPES_TABLE+"."+ColumnKeys.KEY_TITLE + "=?)";
            selectionArgs.add(hilltype);
        }
        if (countryClause != null && !"".equals(where)) {
            if (!"".equals(where))
                where = where + " AND ";
            if(countryClause!=null) where = where + countryClause;
        } else if (countryClause != null) {
            if (!"".equals(where))
                where = where + " AND ";
            where = where + countryClause;
        }
//        if (moreWhere != null && !"".equals(moreWhere) && !"".equals(where)) {
//            where = where + " AND " + moreWhere;
//        } else if (moreWhere != null && !"".equals(moreWhere)) {
//            where = where + moreWhere;
//        }

        Uri dataUri = HillsContentProvider.HILLS_CONTENT_URI;

        String orderBy = params[0].getString("orderBy");
        Cursor dataCursor = mContext.getContentResolver().query(dataUri,new String[]{KEY_HILLNAME,
                KEY_HEIGHTM, KEY_HEIGHTF,
                HILLS_TABLE + "." + KEY_ID,
                KEY_LATITUDE, KEY_LONGITUDE},where,selectionArgs.toArray(new String[selectionArgs.size()]),orderBy);


        Map<Integer,Hill> hillsMap = new LinkedHashMap<>();

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
