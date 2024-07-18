package fpt.huyenptnhe160769.cosplanner.database_old;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@Deprecated
public class CosplannerSQLiteHelper extends SQLiteOpenHelper {
    public static final String CP_COS_BUDGET = "BUDGET";
    public static final String CP_COS_COMPLETE = "COMPLETE";
    public static final String CP_COS_CREATED_AT = "CREATED_AT";
    public static final String CP_COS_DUE_DATE = "DUE_DATE";
    public static final String CP_COS_END_DATE = "END_DATE";
    public static final String CP_COS_ID = "_id";
    public static final String CP_COS_INIT_DATE = "INIT_DATE";
    public static final String CP_COS_NAME = "NAME";
    public static final String CP_COS_NOTES = "NOTES";
    public static final String CP_COS_SERIES = "SERIES";
    public static final String CP_ELEMENT_COST = "COST";
    public static final String CP_ELEMENT_FK_COS = "FK_COS";
    public static final String CP_ELEMENT_HAS_PHOTO = "HAS_PHOTO";
    public static final String CP_ELEMENT_ID = "_id";
    public static final String CP_ELEMENT_NAME = "NAME";
    public static final String CP_ELEMENT_NOTES = "NOTES";
    public static final String CP_ELEMENT_ORDER = "DISP_ORDER";
    public static final String CP_ELEMENT_PERCENT = "PERCENT";
    public static final String CP_ELEMENT_PRIORITY = "PRIORITY";
    public static final String CP_ELEMENT_TIME_HH = "TIME_HH";
    public static final String CP_ELEMENT_TIME_MM = "TIME_MM";
    public static final String CP_ELEMENT_TYPE = "TYPE";
    public static final String CP_ELEMENT_WEIGHT = "WEIGHT";

//    public static final String CP_EVENT_DATE = "DATE";
//    public static final String CP_EVENT_FK_COS = "FK_COS";
//    public static final String CP_EVENT_ID = "_id";
//    public static final String CP_EVENT_NAME = "NAME";
//    public static final String CP_EVENT_NOTES = "NOTES";
//    public static final String CP_EVENT_ORDER = "DISP_ORDER";
//    public static final String CP_EVENT_PLACE = "PLACE";
//    public static final String CP_EVENT_TYPE = "TYPE";
//    public static final String CP_PHOTOSHOOT_CAMEKO = "CAMEKO";
//    public static final String CP_PHOTOSHOOT_DATE = "DATE";
//    public static final String CP_PHOTOSHOOT_FK_COS = "FK_COS";
//    public static final String CP_PHOTOSHOOT_ID = "_id";
//    public static final String CP_PHOTOSHOOT_NOTES = "NOTES";
//    public static final String CP_PHOTOSHOOT_ORDER = "DISP_ORDER";
//    public static final String CP_PHOTOSHOOT_PHOTOS = "PHOTOS";
//    public static final String CP_PHOTOSHOOT_PLACE = "PLACE";
//    public static final String CP_PHOTOSHOOT_TYPE = "TYPE";

    public static final String CP_PHOTO_FK_COS = "FK_COS";
    public static final String CP_PHOTO_ID = "_id";
    public static final String CP_PHOTO_NOTES = "NOTES";
    public static final String CP_PHOTO_ORDER = "DISP_ORDER";
    public static final String CP_PHOTO_URL_IMG = "URL_IMAGE";
    public static final String CP_PHOTO_URL_TMB = "URL_THUMB";

//    public static final String CP_PRIZE_DATE = "DATE";
//    public static final String CP_PRIZE_EVENT = "EVENT";
//    public static final String CP_PRIZE_FK_COS = "FK_COS";
//    public static final String CP_PRIZE_ID = "_id";
//    public static final String CP_PRIZE_NOTES = "NOTES";
//    public static final String CP_PRIZE_ORDER = "DISP_ORDER";
//    public static final String CP_PRIZE_TITLE = "TITLE";

    public static final String CP_PROGRESS_FK_COS = "FK_COS";
    public static final String CP_PROGRESS_ID = "_id";
    public static final String CP_PROGRESS_NOTES = "NOTES";
    public static final String CP_PROGRESS_ORDER = "DISP_ORDER";
    public static final String CP_PROGRESS_URL_IMG = "URL_IMAGE";
    public static final String CP_PROGRESS_URL_TMB = "URL_THUMB";

//    public static final String CP_REFERENCE_FK_COS = "FK_COS";
//    public static final String CP_REFERENCE_ID = "_id";
//    public static final String CP_REFERENCE_NOTES = "NOTES";
//    public static final String CP_REFERENCE_ORDER = "DISP_ORDER";
//    public static final String CP_REFERENCE_URL_IMG = "URL_IMAGE";
//    public static final String CP_REFERENCE_URL_TMB = "URL_THUMB";
//    public static final String CP_TASK_DATE = "DATE";
//    public static final String CP_TASK_FK_COS = "FK_COS";
//    public static final String CP_TASK_ID = "_id";
//    public static final String CP_TASK_NAME = "NAME";
//    public static final String CP_TASK_NOTES = "NOTES";
//    public static final String CP_TASK_ORDER = "DISP_ORDER";
//    public static final String CP_TASK_STATUS = "STATUS";
//    public static final String CP_TASK_TIME = "TIME";

    private static final String CREATE_TABLE_CP_COS = "create table CP_COS(_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL, SERIES TEXT NOT NULL, INIT_DATE TEXT NOT NULL, END_DATE TEXT, COMPLETE TEXT NOT NULL, NOTES TEXT, CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP, BUDGET REAL, DUE_DATE TEXT);";
    private static final String CREATE_TABLE_CP_ELEMENT = "create table CP_ELEMENT(_id INTEGER PRIMARY KEY AUTOINCREMENT, FK_COS INTEGER NOT NULL, TYPE INTEGER NOT NULL, NAME TEXT NOT NULL, PERCENT INTEGER NOT NULL, COST REAL, TIME_HH INTEGER, TIME_MM INTEGER, DISP_ORDER INTEGER, NOTES TEXT, PRIORITY INTEGER, HAS_PHOTO INTEGER, WEIGHT INTEGER);";

    //    private static final String CREATE_TABLE_CP_EVENT = "create table CP_EVENT(_id INTEGER PRIMARY KEY AUTOINCREMENT, FK_COS INTEGER NOT NULL, NAME TEXT NOT NULL, TYPE INTEGER NOT NULL, PLACE TEXT NOT NULL, DATE TEXT NOT NULL, DISP_ORDER INTEGER, NOTES TEXT);";

    private static final String CREATE_TABLE_CP_PHOTO = "create table CP_PHOTO(_id INTEGER PRIMARY KEY AUTOINCREMENT, FK_COS INTEGER NOT NULL, URL_THUMB TEXT NOT NULL, URL_IMAGE TEXT NOT NULL, DISP_ORDER INTEGER, NOTES TEXT);";

//    private static final String CREATE_TABLE_CP_PHOTOSHOOT = "create table CP_PHOTOSHOOT(_id INTEGER PRIMARY KEY AUTOINCREMENT, FK_COS INTEGER NOT NULL, CAMEKO TEXT NOT NULL, TYPE INTEGER NOT NULL, PLACE TEXT NOT NULL, PHOTOS INTEGER NOT NULL, DATE TEXT NOT NULL, DISP_ORDER INTEGER, NOTES TEXT);";
//    private static final String CREATE_TABLE_CP_PRIZE = "create table CP_PRIZE(_id INTEGER PRIMARY KEY AUTOINCREMENT, FK_COS INTEGER NOT NULL, TITLE TEXT NOT NULL, EVENT TEXT NOT NULL, DATE TEXT NOT NULL, DISP_ORDER INTEGER, NOTES TEXT);";

    private static final String CREATE_TABLE_CP_PROGRESS = "create table CP_PROGRESS(_id INTEGER PRIMARY KEY AUTOINCREMENT, FK_COS INTEGER NOT NULL, URL_THUMB TEXT NOT NULL, URL_IMAGE TEXT NOT NULL, DISP_ORDER INTEGER, NOTES TEXT);";

//    private static final String CREATE_TABLE_CP_REFERENCE = "create table CP_REFERENCE(_id INTEGER PRIMARY KEY AUTOINCREMENT, FK_COS INTEGER NOT NULL, URL_THUMB TEXT NOT NULL, URL_IMAGE TEXT NOT NULL, DISP_ORDER INTEGER, NOTES TEXT);";
//    private static final String CREATE_TABLE_CP_TASK = "create table CP_TASK(_id INTEGER PRIMARY KEY AUTOINCREMENT, FK_COS INTEGER NOT NULL, NAME TEXT NOT NULL, STATUS INTEGER NOT NULL, DATE TEXT, TIME TEXT, DISP_ORDER INTEGER, NOTES TEXT);";
    private static final String DATABASE_NAME = "DBCosplanner";
    private static final int DATABASE_VERSION = 3;
    public static final String TABLE_CP_COS = "CP_COS";
    public static final String TABLE_CP_ELEMENT = "CP_ELEMENT";

//    public static final String TABLE_CP_EVENT = "CP_EVENT";
    public static final String TABLE_CP_PHOTO = "CP_PHOTO";

//    public static final String TABLE_CP_PHOTOSHOOT = "CP_PHOTOSHOOT";
//    public static final String TABLE_CP_PRIZE = "CP_PRIZE";

    public static final String TABLE_CP_PROGRESS = "CP_PROGRESS";

//    public static final String TABLE_CP_REFERENCE = "CP_REFERENCE";
//    public static final String TABLE_CP_TASK = "CP_TASK";

    public CosplannerSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 3);
    }

    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_CP_COS);
        database.execSQL(CREATE_TABLE_CP_ELEMENT);

//        database.execSQL(CREATE_TABLE_CP_EVENT);
//        database.execSQL(CREATE_TABLE_CP_PHOTOSHOOT);
//        database.execSQL(CREATE_TABLE_CP_PRIZE);
//        database.execSQL(CREATE_TABLE_CP_REFERENCE);

        database.execSQL(CREATE_TABLE_CP_PHOTO);

//        database.execSQL(CREATE_TABLE_CP_TASK);

        database.execSQL(CREATE_TABLE_CP_PROGRESS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE CP_COS ADD COLUMN BUDGET REAL;");
                db.execSQL("ALTER TABLE CP_COS ADD COLUMN DUE_DATE TEXT;");
                db.execSQL("ALTER TABLE CP_ELEMENT ADD COLUMN DISP_ORDER INTEGER;");
                db.execSQL("ALTER TABLE CP_ELEMENT ADD COLUMN NOTES TEXT;");
                db.execSQL("ALTER TABLE CP_ELEMENT ADD COLUMN PRIORITY INTEGER;");
                db.execSQL("ALTER TABLE CP_ELEMENT ADD COLUMN HAS_PHOTO INTEGER;");

//                db.execSQL("ALTER TABLE CP_EVENT ADD COLUMN DISP_ORDER INTEGER;");
//                db.execSQL("ALTER TABLE CP_EVENT ADD COLUMN NOTES TEXT;");
//                db.execSQL("ALTER TABLE CP_PHOTOSHOOT ADD COLUMN DISP_ORDER INTEGER;");
//                db.execSQL("ALTER TABLE CP_PHOTOSHOOT ADD COLUMN NOTES TEXT;");
//                db.execSQL("ALTER TABLE CP_PRIZE ADD COLUMN DISP_ORDER INTEGER;");
//                db.execSQL("ALTER TABLE CP_PRIZE ADD COLUMN NOTES TEXT;");
//                db.execSQL("ALTER TABLE CP_REFERENCE ADD COLUMN DISP_ORDER INTEGER;");
//                db.execSQL("ALTER TABLE CP_REFERENCE ADD COLUMN NOTES TEXT;");

                db.execSQL("ALTER TABLE CP_PHOTO ADD COLUMN DISP_ORDER INTEGER;");
                db.execSQL("ALTER TABLE CP_PHOTO ADD COLUMN NOTES TEXT;");

//                db.execSQL(CREATE_TABLE_CP_TASK);

                db.execSQL(CREATE_TABLE_CP_PROGRESS);
                db.execSQL("UPDATE CP_COS SET COMPLETE = '2' WHERE COMPLETE = '1'");
                db.execSQL("UPDATE CP_COS SET COMPLETE = '1' WHERE COMPLETE = '0'");
                db.execSQL("UPDATE CP_COS SET BUDGET = 0");
                db.execSQL("UPDATE CP_ELEMENT SET DISP_ORDER = 0");
                db.execSQL("UPDATE CP_ELEMENT SET PRIORITY = 0");
                db.execSQL("UPDATE CP_ELEMENT SET HAS_PHOTO = 0");

//                db.execSQL("UPDATE CP_EVENT SET DISP_ORDER = 0");
//                db.execSQL("UPDATE CP_PHOTOSHOOT SET DISP_ORDER = 0");
//                db.execSQL("UPDATE CP_PRIZE SET DISP_ORDER = 0");
//                db.execSQL("UPDATE CP_REFERENCE SET DISP_ORDER = 0");

                db.execSQL("UPDATE CP_PHOTO SET DISP_ORDER = 0");
                break;
            case 2:
                break;
            default:
                return;
        }
        db.execSQL("ALTER TABLE CP_ELEMENT ADD COLUMN WEIGHT INTEGER;");
        db.execSQL("UPDATE CP_ELEMENT SET WEIGHT = 50");
    }
}
