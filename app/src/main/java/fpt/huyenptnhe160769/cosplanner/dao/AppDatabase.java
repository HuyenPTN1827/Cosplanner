package fpt.huyenptnhe160769.cosplanner.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;

@Database(entities = {
        Cos.class,
        Element.class
},
version = 1,
exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract CosDao cosDao();
    public abstract ElementDao elementDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}
