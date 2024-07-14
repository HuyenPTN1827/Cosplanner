package fpt.huyenptnhe160769.cosplanner.database;

import android.app.backup.BackupManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fpt.huyenptnhe160769.cosplanner.models.Cos;

public class DAOCos {
    private String[] allColumns = {"_id", "NAME", CosplannerSQLiteHelper.CP_COS_SERIES,
            CosplannerSQLiteHelper.CP_COS_INIT_DATE, CosplannerSQLiteHelper.CP_COS_END_DATE,
            CosplannerSQLiteHelper.CP_COS_COMPLETE, "NOTES", CosplannerSQLiteHelper.CP_COS_CREATED_AT,
            CosplannerSQLiteHelper.CP_COS_BUDGET, CosplannerSQLiteHelper.CP_COS_DUE_DATE};
    private SQLiteDatabase database;
    private CosplannerSQLiteHelper dbHelper;
    private DateTime endDate;
    private BackupManager mBackupManager;
    private Period period;
    private DateTime startDate;

    public DAOCos(Context context) {
        this.dbHelper = new CosplannerSQLiteHelper(context);
        this.mBackupManager = new BackupManager(context);
    }

    public void open() throws SQLException {
        this.database = this.dbHelper.getWritableDatabase();
    }

    public void close() {
        this.dbHelper.close();
    }

    public Cos createCos(Cos c) {
        ContentValues values = new ContentValues();
        values.put("NAME", c.getName());
        values.put(CosplannerSQLiteHelper.CP_COS_SERIES, c.getSeries());
        values.put(CosplannerSQLiteHelper.CP_COS_INIT_DATE, c.getInitDateString());
        values.putNull(CosplannerSQLiteHelper.CP_COS_END_DATE);
        values.put(CosplannerSQLiteHelper.CP_COS_COMPLETE, Integer.valueOf(c.getStatusForDB()));
        values.put("NOTES", "");
        values.put(CosplannerSQLiteHelper.CP_COS_BUDGET, Double.valueOf(c.getBudget()));
        if (c.hasDueDate()) {
            values.put(CosplannerSQLiteHelper.CP_COS_DUE_DATE, c.getDueDateString());
        } else {
            values.putNull(CosplannerSQLiteHelper.CP_COS_DUE_DATE);
        }
        Cursor cursor = this.database.query(CosplannerSQLiteHelper.TABLE_CP_COS, this.allColumns,
                "_id = " + this.database.insert(CosplannerSQLiteHelper.TABLE_CP_COS,
                        (String) null, values), (String[]) null, (String) null, (String) null, (String) null);
        cursor.moveToFirst();
        Cos newCos = cursorToCos(cursor);
        cursor.close();
        this.mBackupManager.dataChanged();
        return newCos;
    }

    public long insertCos(Cos c) {
        ContentValues values = new ContentValues();
        values.put("NAME", c.getName());
        values.put(CosplannerSQLiteHelper.CP_COS_SERIES, c.getSeries());
        if (c.hasInitDate()) {
            values.put(CosplannerSQLiteHelper.CP_COS_INIT_DATE, c.getInitDateString());
        } else {
            values.putNull(CosplannerSQLiteHelper.CP_COS_INIT_DATE);
        }
        if (c.hasDueDate()) {
            values.put(CosplannerSQLiteHelper.CP_COS_DUE_DATE, c.getDueDateString());
        } else {
            values.putNull(CosplannerSQLiteHelper.CP_COS_DUE_DATE);
        }
        if (c.hasEndDate()) {
            values.put(CosplannerSQLiteHelper.CP_COS_END_DATE, c.getEndDateString());
        } else {
            values.putNull(CosplannerSQLiteHelper.CP_COS_END_DATE);
        }
        values.put(CosplannerSQLiteHelper.CP_COS_COMPLETE, Integer.valueOf(c.getStatusForDB()));
        values.put("NOTES", c.getNotes());
        values.put(CosplannerSQLiteHelper.CP_COS_BUDGET, Double.valueOf(c.getBudget()));
        long insertId = this.database.insert(CosplannerSQLiteHelper.TABLE_CP_COS, (String) null, values);
        this.mBackupManager.dataChanged();
        return insertId;
    }

    public void updateCos(Cos c) {
        ContentValues values = new ContentValues();
        values.put("NAME", c.getName());
        values.put(CosplannerSQLiteHelper.CP_COS_SERIES, c.getSeries());
        values.put(CosplannerSQLiteHelper.CP_COS_INIT_DATE, c.getInitDateString());
        values.put(CosplannerSQLiteHelper.CP_COS_BUDGET, Double.valueOf(c.getBudget()));
        if (c.hasDueDate()) {
            values.put(CosplannerSQLiteHelper.CP_COS_DUE_DATE, c.getDueDateString());
        } else {
            values.putNull(CosplannerSQLiteHelper.CP_COS_DUE_DATE);
        }
        this.database.update(CosplannerSQLiteHelper.TABLE_CP_COS, values, "_id = " + c.getId(), (String[]) null);
        this.mBackupManager.dataChanged();
    }

    public Cos startCos(Cos c) {
        ContentValues values = new ContentValues();
        values.put(CosplannerSQLiteHelper.CP_COS_COMPLETE, 1);
        values.put(CosplannerSQLiteHelper.CP_COS_INIT_DATE, c.getInitDateString());
        values.put(CosplannerSQLiteHelper.CP_COS_BUDGET, Double.valueOf(c.getBudget()));
        if (c.hasDueDate()) {
            values.put(CosplannerSQLiteHelper.CP_COS_DUE_DATE, c.getDueDateString());
        } else {
            values.putNull(CosplannerSQLiteHelper.CP_COS_DUE_DATE);
        }
        this.database.update(CosplannerSQLiteHelper.TABLE_CP_COS, values, "_id = " + c.getId(), (String[]) null);
        Cursor cursor = this.database.query(CosplannerSQLiteHelper.TABLE_CP_COS, this.allColumns,
                "_id = " + c.getId(), (String[]) null, (String) null, (String) null, (String) null);
        cursor.moveToFirst();
        Cos startedCos = cursorToCos(cursor);
        cursor.close();
        this.mBackupManager.dataChanged();
        return startedCos;
    }

    public Cos finalizeCos(Cos c) {
        ContentValues values = new ContentValues();
        values.put(CosplannerSQLiteHelper.CP_COS_COMPLETE, 2);
        values.put(CosplannerSQLiteHelper.CP_COS_END_DATE, c.getEndDateString());
        values.putNull(CosplannerSQLiteHelper.CP_COS_DUE_DATE);
        this.database.update(CosplannerSQLiteHelper.TABLE_CP_COS, values, "_id = " + c.getId(), (String[]) null);
        Cursor cursor = this.database.query(CosplannerSQLiteHelper.TABLE_CP_COS, this.allColumns,
                "_id = " + c.getId(), (String[]) null, (String) null, (String) null, (String) null);
        cursor.moveToFirst();
        Cos finalizedCos = cursorToCos(cursor);
        cursor.close();
        this.mBackupManager.dataChanged();
        return finalizedCos;
    }

    public Cos modifyCos(Cos c) {
        ContentValues values = new ContentValues();
        values.put(CosplannerSQLiteHelper.CP_COS_COMPLETE, 1);
        values.putNull(CosplannerSQLiteHelper.CP_COS_END_DATE);
        values.putNull(CosplannerSQLiteHelper.CP_COS_DUE_DATE);
        this.database.update(CosplannerSQLiteHelper.TABLE_CP_COS, values, "_id = " + c.getId(), (String[]) null);
        Cursor cursor = this.database.query(CosplannerSQLiteHelper.TABLE_CP_COS, this.allColumns,
                "_id = " + c.getId(), (String[]) null, (String) null, (String) null, (String) null);
        cursor.moveToFirst();
        Cos modifiedCos = cursorToCos(cursor);
        cursor.close();
        this.mBackupManager.dataChanged();
        return modifiedCos;
    }

    public void updateNotes(Cos c) {
        ContentValues values = new ContentValues();
        values.put("NOTES", c.getNotes());
        this.database.update(CosplannerSQLiteHelper.TABLE_CP_COS, values, "_id = " + c.getId(), (String[]) null);
        this.mBackupManager.dataChanged();
    }

    public void deleteCos(long cosID) {
        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_ELEMENT, "FK_COS = " + cosID, (String[]) null);

//        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_TASK, "FK_COS = " + cosID, (String[]) null);
//        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_EVENT, "FK_COS = " + cosID, (String[]) null);
//        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_PHOTOSHOOT, "FK_COS = " + cosID, (String[]) null);
//        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_PRIZE, "FK_COS = " + cosID, (String[]) null);
//        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_REFERENCE, "FK_COS = " + cosID, (String[]) null);

        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_PROGRESS, "FK_COS = " + cosID, (String[]) null);
        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_PHOTO, "FK_COS = " + cosID, (String[]) null);
        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_COS, "_id = " + cosID, (String[]) null);
        this.mBackupManager.dataChanged();
    }

    public Cos getCos(long id) {
        Cursor cursor = this.database.query(CosplannerSQLiteHelper.TABLE_CP_COS, this.allColumns,
                "_id = " + id, (String[]) null, (String) null, (String) null, (String) null);
        cursor.moveToFirst();
        Cos cos = cursorToCos(cursor);
        cursor.close();
        return cos;
    }

    public Cos[] getAllCosByName(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND "))
                    + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, IFNULL(AVG(ELE.PERCENT),0) AS PERCENT " +
                "FROM CP_COS COS LEFT JOIN CP_ELEMENT ELE ON ELE.FK_COS = COS._id " + filter +
                "GROUP BY " + "COS._id, " + "COS.NAME, " + "COS.SERIES, " + "COS.COMPLETE " +
                "ORDER BY " + "COS.NAME " + (order ? "ASC" : "DESC") + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosPercent(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosBySeries(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) +
                    "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, IFNULL(AVG(ELE.PERCENT),0) AS PERCENT FROM CP_COS COS " +
                "LEFT JOIN CP_ELEMENT ELE ON ELE.FK_COS = COS._id " + filter +
                "GROUP BY " + "COS._id, " + "COS.NAME, " + "COS.SERIES, " + "COS.COMPLETE " +
                "ORDER BY " + "COS.SERIES " + (order ? "ASC" : "DESC") + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosPercent(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosByPercent(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        String ord = order ? "DESC" : "ASC";
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, IFNULL(AVG(ELE.PERCENT),0) AS PERCENT " +
                "FROM CP_COS COS LEFT JOIN CP_ELEMENT ELE ON ELE.FK_COS = COS._id " + filter +
                "GROUP BY " + "COS._id, " + "COS.NAME, " + "COS.SERIES, " + "COS.COMPLETE " +
                "ORDER BY " + "COS.COMPLETE " + ord + "," + "AVG(ELE.PERCENT) " + ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosPercent(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosByBrokenPercent(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        String ord = order ? "DESC" : "ASC";
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, COS.COMPLETE, IFNULL (A.PERCENT, 0) AS PERCENT1, " +
                "IFNULL (B.PERCENT, 0) AS PERCENT2, IFNULL (A.PERCENT, 0) + IFNULL (B.PERCENT, 0) AS TOTAL FROM CP_COS COS " +
                "LEFT JOIN ( SELECT COS._id AS ID, IFNULL(AVG(ELE.PERCENT),0) AS PERCENT FROM CP_COS COS " +
                "LEFT JOIN CP_ELEMENT ELE ON ELE.FK_COS = COS._id WHERE ELE.TYPE = 1 " +
                "GROUP BY COS._id ) A ON COS._id = A.ID " +
                "LEFT JOIN ( SELECT COS._id AS ID, IFNULL(AVG(ELE.PERCENT),0) AS PERCENT FROM CP_COS COS " +
                "LEFT JOIN CP_ELEMENT ELE ON ELE.FK_COS = COS._id WHERE ELE.TYPE = 2 GROUP BY COS._id ) B ON COS._id = B.ID " + filter +
                "ORDER BY " + "COS.COMPLETE " + ord + "," + "TOTAL " + ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosBrokenPercent(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

//    public Cos[] getAllCosByTasks(boolean order, int statusFilter, String nameFilter) {
//        String filter = "";
//        if (statusFilter > 0) {
//            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
//        }
//        if (nameFilter != "") {
//            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
//        }
//        List<Cos> cosList = new ArrayList<>();
//        String ord = order ? "DESC" : "ASC";
//        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, COS.COMPLETE, COUNT(TASK._id) - SUM(TASK.STATUS) AS PENDING, SUM(TASK.STATUS) AS READY, COUNT(TASK._id) AS TASKS FROM CP_COS COS LEFT JOIN CP_TASK TASK ON TASK.FK_COS = COS._id " + filter + "GROUP BY " + "COS._id, " + "COS.NAME, " + "COS.SERIES, " + "COS.COMPLETE " + "ORDER BY " + "COUNT(TASK._id) - SUM(TASK.STATUS)" + ord + "," + "SUM(TASK.STATUS) " + ord + "," + "COUNT(TASK._id) " + ord + "," + "COS.COMPLETE " + ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            cosList.add(cursorToCosTask(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
//    }

    public Cos[] getAllCosByInitDate(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, COS.INIT_DATE, COS.END_DATE FROM CP_COS COS " + filter +
                "ORDER BY " + "SUBSTR(COS.INIT_DATE,7,4)||SUBSTR(COS.INIT_DATE,4,2)||SUBSTR(COS.INIT_DATE,1,2) " +
                (order ? "DESC" : "ASC") + ", " + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosDates(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosByEndDate(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        String ord = order ? "DESC" : "ASC";
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, COS.INIT_DATE, COS.END_DATE FROM CP_COS COS " + filter +
                "ORDER BY " + "SUBSTR(COS.END_DATE,7,4)||SUBSTR(COS.END_DATE,4,2)||SUBSTR(COS.END_DATE,1,2) " +
                ord + "," + "SUBSTR(COS.INIT_DATE,7,4)||SUBSTR(COS.INIT_DATE,4,2)||SUBSTR(COS.INIT_DATE,1,2) " +
                ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosDates(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosByDueDate(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, COS.DUE_DATE FROM CP_COS COS " + filter +
                "ORDER BY " + "SUBSTR(COS.DUE_DATE,7,4)||SUBSTR(COS.DUE_DATE,4,2)||SUBSTR(COS.DUE_DATE,1,2) "
                + (order ? "DESC" : "ASC") + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosDueDate(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosByTimeLapse(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        String ord = order ? "DESC" : "ASC";
        Cursor cursor = this.database.rawQuery("SELECT _id, NAME, SERIES, COMPLETE, INIT_DATE, END_DATE, DAYS " +
                "FROM (SELECT COS._id, COS.NAME, COS.SERIES, COS.COMPLETE, COS.INIT_DATE, COS.END_DATE, " +
                "JULIANDAY(DATE('NOW')) - JULIANDAY(DATE(SUBSTR(COS.INIT_DATE,7,4)||'-'||SUBSTR(COS.INIT_DATE,4,2)||'-'||SUBSTR(COS.INIT_DATE,1,2)))  " +
                "AS DAYS, COS.CREATED_AT FROM CP_COS COS WHERE COS.COMPLETE <= 1 " +
                "UNION SELECT COS._id, COS.NAME, COS.SERIES, COS.COMPLETE, COS.INIT_DATE, COS.END_DATE, " +
                "IFNULL(JULIANDAY(DATE(SUBSTR(COS.END_DATE,7,4)||'-'||SUBSTR(COS.END_DATE,4,2)||'-'||SUBSTR(COS.END_DATE,1,2))) - JULIANDAY(DATE(SUBSTR(COS.INIT_DATE,7,4)||'-'||SUBSTR(COS.INIT_DATE,4,2)||'-'||SUBSTR(COS.INIT_DATE,1,2))),0)  AS DAYS, " +
                "COS.CREATED_AT FROM CP_COS COS WHERE COS.COMPLETE = 2 ) " + filter +
                "ORDER BY " + "DAYS " + ord + "," + "COMPLETE " + ord + "," + "CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosTimeLapse(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosByRemainingTime(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        String ord = order ? "DESC" : "ASC";
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, COS.DUE_DATE, case when COS.DUE_DATE IS NOT NULL " +
                "then JULIANDAY(DATE(SUBSTR(COS.DUE_DATE,7,4)||'-'||SUBSTR(COS.DUE_DATE,4,2)||'-'||SUBSTR(COS.DUE_DATE,1,2))) - JULIANDAY(DATE('NOW')) else -1 end AS DAYS, " +
                "COS.CREATED_AT FROM CP_COS COS " + filter +
                "ORDER BY " + "DAYS " + ord + "," + "COS.COMPLETE " + ord + "," + "CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosRemainingTime(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosByBudget(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        String ord = order ? "DESC" : "ASC";
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, IFNULL(COS.BUDGET,0) FROM CP_COS COS " + filter +
                "ORDER BY " + "IFNULL(COS.BUDGET,0) " + ord + "," + "COS.COMPLETE " + ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosBudget(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosByTotalCost(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        String ord = order ? "DESC" : "ASC";
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, IFNULL(SUM(ELE.COST),0) AS COST FROM CP_COS COS " +
                "LEFT JOIN CP_ELEMENT ELE ON ELE.FK_COS = COS._id " + filter +
                "GROUP BY " + "COS._id, " + "COS.NAME, " + "COS.SERIES, " + "COS.COMPLETE " +
                "ORDER BY " + "SUM(ELE.COST) " + ord + "," + "COS.COMPLETE " + ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosCost(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

    public Cos[] getAllCosByTotalTime(boolean order, int statusFilter, String nameFilter) {
        String filter = "";
        if (statusFilter > 0) {
            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
        }
        if (nameFilter != "") {
            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
        }
        List<Cos> cosList = new ArrayList<>();
        String ord = order ? "DESC" : "ASC";
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, IFNULL((SUM((ELE.TIME_HH * 60) + ELE.TIME_MM) / 60),0) AS HH, " +
                "IFNULL((SUM((ELE.TIME_HH * 60) + ELE.TIME_MM) % 60),0) AS MM FROM CP_COS COS " +
                "LEFT JOIN CP_ELEMENT ELE ON ELE.FK_COS = COS._id " + filter +
                "GROUP BY " + "COS._id, " + "COS.NAME, " + "COS.SERIES, " + "COS.COMPLETE " +
                "ORDER BY " + "SUM((ELE.TIME_HH * 60) + ELE.TIME_MM) " + ord + "," + "COS.COMPLETE " + ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cosList.add(cursorToCosTime(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
    }

//    public Cos[] getAllCosByEvents(boolean order, int statusFilter, String nameFilter) {
//        String filter = "";
//        if (statusFilter > 0) {
//            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
//        }
//        if (nameFilter != "") {
//            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
//        }
//        List<Cos> cosList = new ArrayList<>();
//        String ord = order ? "DESC" : "ASC";
//        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, COS.COMPLETE, COUNT(EVENT._id) AS EVENTS FROM CP_COS COS LEFT JOIN CP_EVENT EVENT ON EVENT.FK_COS = COS._id " + filter + "GROUP BY " + "COS._id, " + "COS.NAME, " + "COS.SERIES, " + "COS.COMPLETE " + "ORDER BY " + "COUNT(EVENT._id) " + ord + "," + "COS.COMPLETE " + ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            cosList.add(cursorToCosEvent(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
//    }

//    public Cos[] getAllCosByPhotoshoots(boolean order, int statusFilter, String nameFilter) {
//        String filter = "";
//        if (statusFilter > 0) {
//            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
//        }
//        if (nameFilter != "") {
//            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
//        }
//        List<Cos> cosList = new ArrayList<>();
//        String ord = order ? "DESC" : "ASC";
//        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, COS.COMPLETE, COUNT(PHOTO._id) AS PHOTOSHOOTS, IFNULL(SUM(PHOTO.PHOTOS),0) AS PHOTOS FROM CP_COS COS LEFT JOIN CP_PHOTOSHOOT PHOTO ON PHOTO.FK_COS = COS._id " + filter + "GROUP BY " + "COS._id, " + "COS.NAME, " + "COS.SERIES, " + "COS.COMPLETE " + "ORDER BY " + "COUNT(PHOTO._id) " + ord + "," + "SUM(PHOTO.PHOTOS) " + ord + "," + "COS.COMPLETE " + ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            cosList.add(cursorToCosPhotoshoots(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
//    }

//    public Cos[] getAllCosByPrizes(boolean order, int statusFilter, String nameFilter) {
//        String filter = "";
//        if (statusFilter > 0) {
//            filter = "WHERE COS.COMPLETE = " + (statusFilter - 1);
//        }
//        if (nameFilter != "") {
//            filter = String.valueOf(String.valueOf(filter) + (filter == "" ? "WHERE " : " AND ")) + "COS.NAME LIKE '%" + nameFilter.replace("'", "''") + "%' ";
//        }
//        List<Cos> cosList = new ArrayList<>();
//        String ord = order ? "DESC" : "ASC";
//        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, COS.COMPLETE, COUNT(PRIZE._id) AS PRIZES FROM CP_COS COS LEFT JOIN CP_PRIZE PRIZE ON PRIZE.FK_COS = COS._id " + filter + "GROUP BY " + "COS._id, " + "COS.NAME, " + "COS.SERIES, " + "COS.COMPLETE " + "ORDER BY " + "COUNT(PRIZE._id) " + ord + "," + "COS.COMPLETE " + ord + "," + "COS.CREATED_AT " + (order ? "ASC" : "DESC"), (String[]) null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            cosList.add(cursorToCosPrize(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return (Cos[]) cosList.toArray(new Cos[cosList.size()]);
//    }

    public double getTotalPercent(long fkCos) {
        double res = 0.0d;
        Cursor cursor = this.database.rawQuery("SELECT IFNULL(AVG(ELE.PERCENT),0) AS PERCENT " +
                "FROM CP_ELEMENT ELE WHERE ELE.FK_COS = " + fkCos, (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            res = cursor.getDouble(0);
            cursor.moveToNext();
        }
        cursor.close();
        return res;
    }

    public Cos getTimeLapse(long cosID) {
        Cos cos = null;
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, COS.INIT_DATE, COS.END_DATE FROM CP_COS COS " +
                "WHERE COS._id = " + cosID, (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cos = cursorToCosTimeLapse(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return cos;
    }

    public Cos getRemainingTime(long cosID) {
        Cos cos = null;
        Cursor cursor = this.database.rawQuery("SELECT COS._id, COS.NAME, COS.SERIES, " +
                "COS.COMPLETE, COS.DUE_DATE FROM CP_COS COS WHERE COS._id = " + cosID, (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cos = cursorToCosRemainingTime(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return cos;
    }

    public int getNumberOfCosplays() {
        int res = 0;
        Cursor cursor = this.database.rawQuery("SELECT COUNT(*) FROM CP_COS COS", (String[]) null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            res = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        return res;
    }

    private Cos cursorToCos(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        if (cursor.getString(3) != null && !cursor.getString(3).isEmpty()) {
            String[] arrayDate = cursor.getString(3).split("/");
            cos.setInitDate(Integer.parseInt(arrayDate[0]), Integer.parseInt(arrayDate[1]), Integer.parseInt(arrayDate[2]));
        }
        if (cursor.getString(4) != null && !cursor.getString(4).isEmpty()) {
            String[] arrayDate2 = cursor.getString(4).split("/");
            cos.setEndDate(Integer.parseInt(arrayDate2[0]), Integer.parseInt(arrayDate2[1]), Integer.parseInt(arrayDate2[2]));
        }
        cos.setStatus(cursor.getInt(5));
        if (cursor.getString(6) != null && !cursor.getString(6).isEmpty()) {
            cos.setNotes(cursor.getString(6));
        }
        cos.setBudget(cursor.getDouble(8));
        if (cursor.getString(9) != null && !cursor.getString(9).isEmpty()) {
            String[] arrayDate3 = cursor.getString(9).split("/");
            cos.setDueDate(Integer.parseInt(arrayDate3[0]), Integer.parseInt(arrayDate3[1]), Integer.parseInt(arrayDate3[2]));
        }
        return cos;
    }

    private Cos cursorToCosPercent(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
        cos.setTotalPercent((double) cursor.getFloat(4));
        return cos;
    }

    private Cos cursorToCosBrokenPercent(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
        cos.setTotalPercentBuy((double) cursor.getFloat(4));
//        cos.setTotalPercentMake((double) cursor.getFloat(5));
        return cos;
    }

    private Cos cursorToCosTask(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
//        cos.setTasksPending(cursor.getInt(4));
//        cos.setTasksReady(cursor.getInt(5));
//        cos.setTasksTotal(cursor.getInt(6));
        return cos;
    }

    private Cos cursorToCosDates(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
        if (cursor.getString(4) != null && !cursor.getString(4).isEmpty()) {
            String[] arrayDate = cursor.getString(4).split("/");
            cos.setInitDate(Integer.parseInt(arrayDate[0]), Integer.parseInt(arrayDate[1]), Integer.parseInt(arrayDate[2]));
        }
        if (cursor.getString(5) != null && !cursor.getString(5).isEmpty()) {
            String[] arrayDate2 = cursor.getString(5).split("/");
            cos.setEndDate(Integer.parseInt(arrayDate2[0]), Integer.parseInt(arrayDate2[1]), Integer.parseInt(arrayDate2[2]));
        }
        return cos;
    }

    private Cos cursorToCosDueDate(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
        if (cursor.getString(4) != null && !cursor.getString(4).isEmpty()) {
            String[] arrayDate = cursor.getString(4).split("/");
            cos.setDueDate(Integer.parseInt(arrayDate[0]), Integer.parseInt(arrayDate[1]), Integer.parseInt(arrayDate[2]));
        }
        return cos;
    }

    private Cos cursorToCosTimeLapse(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
        if (!cos.isPlanned()) {
            if (cursor.getString(4) != null && !cursor.getString(4).isEmpty()) {
                String[] arrayDate = cursor.getString(4).split("/");
                cos.setInitDate(Integer.parseInt(arrayDate[0]), Integer.parseInt(arrayDate[1]), Integer.parseInt(arrayDate[2]));
                this.startDate = new DateTime(Integer.parseInt(arrayDate[2]), Integer.parseInt(arrayDate[1]),
                        Integer.parseInt(arrayDate[0]), 0, 0, 0, 0);
            }
            if (cursor.getString(5) == null || cursor.getString(5).isEmpty()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                this.endDate = new DateTime(cal.get(1), cal.get(2) + 1, cal.get(5), 0, 0, 0, 0);
            } else {
                String[] arrayDate2 = cursor.getString(5).split("/");
                cos.setEndDate(Integer.parseInt(arrayDate2[0]), Integer.parseInt(arrayDate2[1]), Integer.parseInt(arrayDate2[2]));
                this.endDate = new DateTime(Integer.parseInt(arrayDate2[2]), Integer.parseInt(arrayDate2[1]),
                        Integer.parseInt(arrayDate2[0]), 0, 0, 0, 0);
            }
            this.period = new Period((ReadableInstant) this.startDate, (ReadableInstant) this.endDate, PeriodType.yearMonthDay());
            cos.setTimeDD(this.period.getDays());
            cos.setTimeMM(this.period.getMonths());
            cos.setTimeYY(this.period.getYears());
        }
        return cos;
    }

    private Cos cursorToCosRemainingTime(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
        if (cursor.getString(4) != null && !cursor.getString(4).isEmpty()) {
            String[] arrayDate = cursor.getString(4).split("/");
            cos.setDueDate(Integer.parseInt(arrayDate[0]), Integer.parseInt(arrayDate[1]), Integer.parseInt(arrayDate[2]));
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            this.startDate = new DateTime(cal.get(1), cal.get(2) + 1, cal.get(5), 0, 0, 0, 0);
            this.endDate = new DateTime(Integer.parseInt(arrayDate[2]), Integer.parseInt(arrayDate[1]),
                    Integer.parseInt(arrayDate[0]), 0, 0, 0, 0);
            this.period = new Period((ReadableInstant) this.startDate, (ReadableInstant) this.endDate, PeriodType.yearMonthDay());
            cos.setRTimeDD(this.period.getDays());
            cos.setRTimeMM(this.period.getMonths());
            cos.setRTimeYY(this.period.getYears());
        }
        return cos;
    }

    private Cos cursorToCosBudget(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
        cos.setBudget(cursor.getDouble(4));
        return cos;
    }

    private Cos cursorToCosCost(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
        cos.setTotalCost(cursor.getDouble(4));
        return cos;
    }

    private Cos cursorToCosTime(Cursor cursor) {
        Cos cos = new Cos();
        cos.setId(cursor.getLong(0));
        cos.setName(cursor.getString(1));
        cos.setSeries(cursor.getString(2));
        cos.setStatus(cursor.getInt(3));
        cos.setTimeHRS(cursor.getInt(4));
        cos.setTimeMIN(cursor.getInt(5));
        return cos;
    }

//    private Cos cursorToCosEvent(Cursor cursor) {
//        Cos cos = new Cos();
//        cos.setId(cursor.getLong(0));
//        cos.setName(cursor.getString(1));
//        cos.setSeries(cursor.getString(2));
//        cos.setStatus(cursor.getInt(3));
//        cos.setNumberOfEvents(cursor.getInt(4));
//        return cos;
//    }
//
//    private Cos cursorToCosPhotoshoots(Cursor cursor) {
//        Cos cos = new Cos();
//        cos.setId(cursor.getLong(0));
//        cos.setName(cursor.getString(1));
//        cos.setSeries(cursor.getString(2));
//        cos.setStatus(cursor.getInt(3));
//        cos.setNumberOfPhotoshoots(cursor.getInt(4));
//        cos.setNumberOfPhotos(cursor.getInt(5));
//        return cos;
//    }
//
//    private Cos cursorToCosPrize(Cursor cursor) {
//        Cos cos = new Cos();
//        cos.setId(cursor.getLong(0));
//        cos.setName(cursor.getString(1));
//        cos.setSeries(cursor.getString(2));
//        cos.setStatus(cursor.getInt(3));
//        cos.setNumberOfPrizes(cursor.getInt(4));
//        return cos;
//    }
}
