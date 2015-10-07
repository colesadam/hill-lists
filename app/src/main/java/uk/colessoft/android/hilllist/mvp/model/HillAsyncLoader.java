package uk.colessoft.android.hilllist.mvp.model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.colessoft.android.hilllist.contentprovider.HillsContentProvider;
import uk.colessoft.android.hilllist.objects.Hill;

import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_AREA;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_CLASSIFICATION;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_CLIMBED;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_COLGRIDREF;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_COLHEIGHT;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_COMMENTS;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_DATECLIMBED;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_DROP;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_FEATURE;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_GEOGRAPH;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_GRIDREF;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_GRIDREF10;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HEIGHTF;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HEIGHTM;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HILLBAGGING;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_HILLNAME;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_LATITUDE;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_LONGITUDE;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_MAP;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_MAP25;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_NOTES;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_OBSERVATIONS;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_REGION;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_REVISION;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_SECTION;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_STREETMAP;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_SURVEY;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_XCOORD;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_XSECTION;
import static uk.colessoft.android.hilllist.database.ColumnKeys.KEY_YCOORD;

public class HillAsyncLoader extends AsyncTask<Long,Void,Hill> {

    private Context mContext;
    private HillLoaderListener listener;

    public interface HillLoaderListener {
        public void onSuccess(Hill hill);

        public void onError(Exception e);
    }

    public HillAsyncLoader(HillLoaderListener listener, Context context) {
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    protected Hill doInBackground(Long... params) {
        long hillId=params[0];
        Uri dataUri = Uri.parse(HillsContentProvider.HILLS_CONTENT_URI + "/"
                + hillId);

        Cursor cursor = mContext.getContentResolver().query(dataUri, null, null, null, null);
        
        if(cursor.moveToFirst())
            return getHill(cursor);
        else return null;
    }

    @Override protected void onPostExecute(Hill hill) {

        if (isCancelled() || hill == null) {
            return;
        }

            listener.onSuccess(hill);

    }

    private Hill getHill(Cursor cursor) throws SQLException {

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
        int _id = cursor.getInt(0);

        String _section = cursor.getString(cursor
                .getColumnIndex(KEY_XSECTION));
        String hillname = cursor.getString(cursor
                .getColumnIndex(KEY_HILLNAME));
        String section = cursor.getString(cursor
                .getColumnIndex(KEY_SECTION));
        String region = cursor.getString(cursor
                .getColumnIndex(KEY_REGION));
        String area = cursor.getString(cursor
                .getColumnIndex(KEY_AREA));
        float heightm = cursor.getFloat(cursor
                .getColumnIndex(KEY_HEIGHTM));
        float heightf = cursor.getFloat(cursor
                .getColumnIndex(KEY_HEIGHTF));
        String map = cursor.getString(cursor
                .getColumnIndex(KEY_MAP));
        String map25 = cursor.getString(cursor
                .getColumnIndex(KEY_MAP25));
        String gridref = cursor.getString(cursor
                .getColumnIndex(KEY_GRIDREF));
        String colgridref = cursor.getString(cursor
                .getColumnIndex(KEY_COLGRIDREF));
        float colheight = cursor.getFloat(cursor
                .getColumnIndex(KEY_COLHEIGHT));
        float drop = cursor.getFloat(cursor
                .getColumnIndex(KEY_DROP));
        String gridref10 = cursor.getString(cursor
                .getColumnIndex(KEY_GRIDREF10));
        String feature = cursor.getString(cursor
                .getColumnIndex(KEY_FEATURE));
        String observations = cursor.getString(cursor
                .getColumnIndex(KEY_OBSERVATIONS));
        String survey = cursor.getString(cursor
                .getColumnIndex(KEY_SURVEY));
        String climbed = cursor.getString(cursor
                .getColumnIndex(KEY_CLIMBED));
        String classification = cursor.getString(cursor
                .getColumnIndex(KEY_CLASSIFICATION));
        long revision = cursor.getLong(cursor
                .getColumnIndex(KEY_REVISION));
        String comments = cursor.getString(cursor
                .getColumnIndex(KEY_COMMENTS));
        int xcoord = cursor.getInt(cursor
                .getColumnIndex(KEY_XCOORD));
        int ycoord = cursor.getInt(cursor
                .getColumnIndex(KEY_YCOORD));
        double latitude = cursor.getDouble(cursor
                .getColumnIndex(KEY_LATITUDE));
        double longitude = cursor.getDouble(cursor
                .getColumnIndex(KEY_LONGITUDE));
        String streetmap = cursor.getString(cursor
                .getColumnIndex(KEY_STREETMAP));
        String getamap = cursor.getString(cursor
                .getColumnIndex(KEY_GEOGRAPH));
        String hillBagging = cursor.getString(cursor
                .getColumnIndex(KEY_HILLBAGGING));
        Date dateClimbed = null;

        try {
            dateClimbed = iso8601Format.parse(cursor.getString(cursor
                    .getColumnIndex(KEY_DATECLIMBED)));
        } catch (java.text.ParseException e) {
            Log.e(this.toString(), e.toString());
        } catch (NullPointerException npe) {
            // ignore
        }

        String notes = cursor.getString(cursor
                .getColumnIndex(KEY_NOTES));

        Hill result = new Hill(_id, _section, hillname, section, region, area,
                heightm, heightf, map, map25, gridref, colgridref, colheight,
                drop, gridref10, feature, observations, survey, climbed,
                classification, new Date(revision), comments, xcoord, ycoord,
                latitude, longitude, streetmap, getamap, hillBagging,
                dateClimbed, notes);
        return result;

    }
}
