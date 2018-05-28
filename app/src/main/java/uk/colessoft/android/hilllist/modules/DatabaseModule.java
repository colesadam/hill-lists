package uk.colessoft.android.hilllist.modules;


import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.dao.HillDao;
import uk.colessoft.android.hilllist.dao.HillTypeDao;
import uk.colessoft.android.hilllist.dao.TypeLinkDao;
import uk.colessoft.android.hilllist.database.BaggingTable;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.database.HillsDatabase;
import uk.colessoft.android.hilllist.database.HillsDatabaseHelper;
import uk.colessoft.android.hilllist.database.HillsLocalDatasource;
import uk.colessoft.android.hilllist.database.HillsTables;

import static android.arch.persistence.room.RoomDatabase.JournalMode.TRUNCATE;

@Module
public class DatabaseModule {



    @Provides
    @Singleton
    HillsDatabase providesRoomDatabase(Application application) {
        return Room.databaseBuilder(application, HillsDatabase.class, "hill-list.db").addMigrations(HillsDatabase.MIGRATION_2_3)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        HillsTables.onCreate(db, application);//, handler, 9999999);
                    }
                }).setJournalMode(TRUNCATE).build();
    }

    @Provides
    @Singleton
    HillDao providesHillDao(HillsDatabase database) {
        return database.hillDao();
    }

    @Provides
    @Singleton
    HillTypeDao providesHillTypeDao(HillsDatabase database) {
        return database.hillTypeDao();
    }

    @Provides
    @Singleton
    TypeLinkDao providesTypeLinkDao(HillsDatabase database) {
        return database.typeLinkDao();
    }
}
