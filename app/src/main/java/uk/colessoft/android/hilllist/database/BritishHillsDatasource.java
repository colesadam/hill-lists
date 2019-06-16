package uk.colessoft.android.hilllist.database;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import java.io.Reader;
import java.util.Date;

import uk.colessoft.android.hilllist.model.HillDetail;

public interface BritishHillsDatasource {
    void markHillClimbed(long hillNumber, Date dateClimbed, String notes);

    void markHillNotClimbed(long hillNumber);

    Cursor getAllHillsCursor();

    Cursor getHillsForNearby();

    Cursor getBaggedHillList();

    Cursor getHillGroup(String groupId, String countryClause,
                        String moreWhere, String orderBy, int filter);

    Cursor getT100(String moreWhere, String orderBy, int filter);

    HillDetail getHill(long _rowIndex) throws SQLException;

    LiveData<HillDetail> getHillReactive (long hillId);


    void importBagging(final Reader fileReader);

    void touch(Handler handler);

}
