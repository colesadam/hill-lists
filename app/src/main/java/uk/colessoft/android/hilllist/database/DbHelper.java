package uk.colessoft.android.hilllist.database;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;

import java.util.Date;

import uk.colessoft.android.hilllist.model.Hill;

public interface DbHelper {
    void markHillClimbed(int hillNumber, Date dateClimbed, String notes);

    void markHillNotClimbed(int hillNumber);

    Cursor getAllHillsCursor();

    Cursor getHillsForNearby();

    Cursor getBaggedHillList();

    Cursor getHillGroup(String groupId, String countryClause,
                        String moreWhere, String orderBy, int filter);

    Cursor getT100(String moreWhere, String orderBy, int filter);

    Hill getHill(long _rowIndex) throws SQLException;


    void importBagging(final String filePath);

    void touch(Handler handler);
}
