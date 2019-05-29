package uk.colessoft.android.hilllist.modules;


import android.app.Application;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.dao.HillDao;
import uk.colessoft.android.hilllist.dao.HillDetailDao;
import uk.colessoft.android.hilllist.dao.HillTypeDao;
import uk.colessoft.android.hilllist.dao.TypeLinkDao;
import uk.colessoft.android.hilllist.database.HillsDatabase;
import uk.colessoft.android.hilllist.database.HillsTables;

import static androidx.room.RoomDatabase.JournalMode.TRUNCATE;

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

    @Provides
    @Singleton
    HillDetailDao providesHillDetailDao(HillsDatabase database) {
        return database.hillDetailDao();
    }
}
