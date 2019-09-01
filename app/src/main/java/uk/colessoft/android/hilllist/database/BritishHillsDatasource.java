package uk.colessoft.android.hilllist.database;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import java.io.Reader;
import java.util.Date;
import java.util.List;

import uk.colessoft.android.hilllist.dao.CountryClause;
import uk.colessoft.android.hilllist.domain.HillDetail;

public interface BritishHillsDatasource {

    void markHillClimbed(long hillNumber, Date dateClimbed, String notes);

    void markHillNotClimbed(long hillNumber);

    Cursor getAllHillsCursor();

    Cursor getHillsForNearby();

    Cursor getBaggedHillList();

    HillDetail getHill(long _rowIndex) throws SQLException;

    LiveData<HillDetail> getHillReactive(long hillId);

    LiveData<List<HillDetail>> getHills(String groupId, CountryClause country, String moreFilters);

    LiveData<List<HillDetail>> getHills(Float latitude, Float Longitude, Float range);

    void importBagging(final Reader fileReader);

}
