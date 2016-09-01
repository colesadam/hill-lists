package uk.colessoft.android.hilllist.database;

import android.database.Cursor;
import android.database.SQLException;

import java.util.Date;

import uk.colessoft.android.hilllist.model.Hill;

public interface DbHelper {
    void markHillClimbed(int hillNumber, Date dateClimbed, String notes);

    void markHillNotClimbed(int hillNumber);

    Cursor getAllHillsCursor();

    Cursor getBaggedHillList();

    Cursor getHillsbyPartialName(String where, String orderBy);

    Cursor getHillGroup(String groupId, String countryClause,
                        String moreWhere, String orderBy, int filter);

    Cursor setCursorHill(long _rowIndex) throws SQLException;

    Hill getHill(long _rowIndex) throws SQLException;

    int getClimbedCount(String hilltype, String countryClause,
                        String moreWhere);
}
