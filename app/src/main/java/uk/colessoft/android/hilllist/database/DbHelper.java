package uk.colessoft.android.hilllist.database;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;

import org.joda.time.LocalDate;

import java.util.Date;
import java.util.List;

import rx.Observable;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.model.TinyHill;

public interface DbHelper {

    Observable<Long> markHillClimbed(int hillNumber, LocalDate dateClimbed, String notes);

    Observable<Integer> markHillNotClimbed(int hillNumber);

    Cursor getAllHillsCursor();

    Cursor getHillsForNearby();

    Observable<List<TinyHill>> getNearbyHills(double lat1, double lon1, double nearRadius);

    Cursor getBaggedHillList();

    Cursor getHillGroup(String groupId, String countryClause,
                        String moreWhere, String orderBy, int filter);

    Cursor getT100(String moreWhere, String orderBy, int filter);

    Observable<Hill> getHill(long _rowIndex) throws SQLException;

    Observable<List<TinyHill>> getHills(String groupId, String countryClause,
                                        String moreWhere, String orderBy, int filter);

    void importBagging(final String filePath);

    void touch(Handler handler);

    void close();
}
