package fpt.huyenptnhe160769.cosplanner.dao;

import androidx.room.Database;
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
    public abstract CosDao cosDao();
    public abstract ElementDao elementDao();
}
