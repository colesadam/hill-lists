package uk.colessoft.android.hilllist.database;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;

import java.io.Reader;
import java.util.Date;

import uk.colessoft.android.hilllist.model.HillDetail;

public interface BritishHillsDatasource {
    void markHillClimbed(int hillNumber, Date dateClimbed, String notes);

    void markHillNotClimbed(int hillNumber);

    Cursor getAllHillsCursor();

    Cursor getHillsForNearby();

    Cursor getBaggedHillList();

    Cursor getHillGroup(String groupId, String countryClause,
                        String moreWhere, String orderBy, int filter);

    Cursor getT100(String moreWhere, String orderBy, int filter);

    HillDetail getHill(long _rowIndex) throws SQLException;


    void importBagging(final Reader fileReader);

    void touch(Handler handler);

}
