package fpt.huyenptnhe160769.cosplanner.database;

import android.app.backup.BackupManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fpt.huyenptnhe160769.cosplanner.models.Element;

public class DAOElement {
    private String[] allColumns = {"_id", "FK_COS", "NAME", CosplannerSQLiteHelper.CP_ELEMENT_COST,
            CosplannerSQLiteHelper.CP_ELEMENT_TIME_HH, CosplannerSQLiteHelper.CP_ELEMENT_TIME_MM,
            "DISP_ORDER", "NOTES", CosplannerSQLiteHelper.CP_ELEMENT_PRIORITY,
            CosplannerSQLiteHelper.CP_ELEMENT_HAS_PHOTO};
    private SQLiteDatabase database;
    private CosplannerSQLiteHelper dbHelper;
    private BackupManager mBackupManager;

    public DAOElement(Context context) {
        this.dbHelper = new CosplannerSQLiteHelper(context);
        this.mBackupManager = new BackupManager(context);
    }

    public void open() throws SQLException {
        this.database = this.dbHelper.getWritableDatabase();
    }

    public void close() {
        this.dbHelper.close();
    }

    public Element getElement(long id) {
        Cursor cursor = this.database.query(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, this.allColumns,
                "_id = " + id, (String[]) null, (String) null, (String) null, (String) null);
        cursor.moveToFirst();
        Element element = cursorToElement(cursor);
        cursor.close();
        return element;
    }

    public long createElement(Element e) {
        int i;
        int i2 = 1;
        ContentValues values = new ContentValues();
        values.put("FK_COS", Long.valueOf(e.getFkCos()));
//        values.put("TYPE", Integer.valueOf(e.getType()));
//        values.put(CosplannerSQLiteHelper.CP_ELEMENT_WEIGHT, Integer.valueOf(e.getWeight()));
        values.put("NAME", e.getName());
//        values.put(CosplannerSQLiteHelper.CP_ELEMENT_PERCENT, Integer.valueOf(e.getPercent()));
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_COST, Double.valueOf(e.getCost()));
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_TIME_HH, Integer.valueOf(e.getTimeHH()));
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_TIME_MM, Integer.valueOf(e.getTimeMM()));
        values.put("DISP_ORDER", Integer.valueOf(e.getOrder()));
        values.put("NOTES", e.getNotes());
        if (e.isPriority()) {
            i = 1;
        } else {
            i = 0;
        }
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_PRIORITY, Integer.valueOf(i));
        if (!e.hasPhoto()) {
            i2 = 0;
        }
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_HAS_PHOTO, Integer.valueOf(i2));
        long insertId = this.database.insert(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, (String) null, values);
        this.mBackupManager.dataChanged();
        return insertId;
    }

    public void updateElement(Element e) {
        int i;
        int i2 = 1;
        ContentValues values = new ContentValues();
        values.put("NAME", e.getName());
//        values.put(CosplannerSQLiteHelper.CP_ELEMENT_PERCENT, Integer.valueOf(e.getPercent()));
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_TIME_HH, Integer.valueOf(e.getTimeHH()));
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_TIME_MM, Integer.valueOf(e.getTimeMM()));
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_COST, Double.valueOf(e.getCost()));
        values.put("DISP_ORDER", Integer.valueOf(e.getOrder()));
        values.put("NOTES", e.getNotes());
        if (e.isPriority()) {
            i = 1;
        } else {
            i = 0;
        }
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_PRIORITY, Integer.valueOf(i));
        if (!e.hasPhoto()) {
            i2 = 0;
        }
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_HAS_PHOTO, Integer.valueOf(i2));
        this.database.update(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, values, "_id = " + e.getId(), (String[]) null);
        this.mBackupManager.dataChanged();
    }

    public void setHasPhoto(long elementId, boolean b) {
        ContentValues values = new ContentValues();
        values.put(CosplannerSQLiteHelper.CP_ELEMENT_HAS_PHOTO, Integer.valueOf(b ? 1 : 0));
        this.database.update(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, values, "_id = " + elementId, (String[]) null);
    }

//    public void switchToBuy(Element e) {
//        ContentValues values = new ContentValues();
//        values.put("TYPE", 1);
//        values.put(CosplannerSQLiteHelper.CP_ELEMENT_PERCENT, 0);
//        values.put(CosplannerSQLiteHelper.CP_ELEMENT_TIME_HH, 0);
//        values.put(CosplannerSQLiteHelper.CP_ELEMENT_TIME_MM, 0);
//        this.database.update(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, values, "_id = " + e.getId(), (String[]) null);
//        this.mBackupManager.dataChanged();
//    }
//
//    public void switchToMake(Element e) {
//        ContentValues values = new ContentValues();
//        values.put("TYPE", 2);
//        values.put(CosplannerSQLiteHelper.CP_ELEMENT_PERCENT, 0);
//        values.put(CosplannerSQLiteHelper.CP_ELEMENT_COST, 0);
//        this.database.update(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, values, "_id = " + e.getId(), (String[]) null);
//        this.mBackupManager.dataChanged();
//    }

    public void deleteElement(long id) {
        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, "_id = " + id, (String[]) null);
        this.mBackupManager.dataChanged();
    }

    public void deleteAllElements(long fkCos) {
        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, "FK_COS = " + fkCos, (String[]) null);
        this.mBackupManager.dataChanged();
    }

    public List<Element> getAllElements(long fkCos) {
        List<Element> elementList = new ArrayList<>();
        Cursor cursor = this.database.query(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, this.allColumns,
                "FK_COS = " + fkCos, (String[]) null, (String) null, (String) null, "TYPE ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            elementList.add(cursorToElement(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return elementList;
    }

    private Element cursorToElement(Cursor cursor) {
        boolean z;
        boolean z2 = true;
        Element element = new Element();
        element.setId(cursor.getLong(0));
        element.setFkCos(cursor.getLong(1));
//        element.setType(cursor.getInt(2));
        element.setName(cursor.getString(3));
//        element.setPercent(cursor.getInt(4));
        element.setCost(cursor.getDouble(5));
        element.setTimeHH(cursor.getInt(6));
        element.setTimeMM(cursor.getInt(7));
        element.setOrder(cursor.getInt(8));
        if (cursor.getString(9) == null || cursor.getString(9).isEmpty()) {
            element.setNotes("");
        } else {
            element.setNotes(cursor.getString(9));
        }
        if (cursor.getInt(10) == 1) {
            z = true;
        } else {
            z = false;
        }
        element.setPriority(z);
        if (cursor.getInt(11) != 1) {
            z2 = false;
        }
        element.setHasPhoto(z2);
//        element.setWeight(cursor.getInt(12));
        return element;
    }
}
