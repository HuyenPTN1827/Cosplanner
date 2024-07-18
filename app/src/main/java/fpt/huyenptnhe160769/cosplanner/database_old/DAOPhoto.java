package fpt.huyenptnhe160769.cosplanner.database_old;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import fpt.huyenptnhe160769.cosplanner.models_old.CPImage;

@Deprecated
public class DAOPhoto {
    private String[] allColumns = {"_id", "FK_COS", "URL_THUMB", "URL_IMAGE", "DISP_ORDER", "NOTES"};
    private SQLiteDatabase database;
    private CosplannerSQLiteHelper dbHelper;

    public DAOPhoto(Context context) {
        this.dbHelper = new CosplannerSQLiteHelper(context);
    }

    public void open() throws SQLException {
        this.database = this.dbHelper.getWritableDatabase();
    }

    public void close() {
        this.dbHelper.close();
    }

    public void createPhoto(CPImage i) {
        ContentValues values = new ContentValues();
        values.put("FK_COS", Long.valueOf(i.getFkCos()));
        values.put("URL_THUMB", i.getUrlThumb());
        values.put("URL_IMAGE", i.getUrlImage());
        values.put("DISP_ORDER", Integer.valueOf(i.getOrder()));
        if (i.getNotes() == null || i.getNotes().isEmpty()) {
            values.putNull("NOTES");
        } else {
            values.put("NOTES", i.getNotes());
        }
        this.database.insert(CosplannerSQLiteHelper.TABLE_CP_PHOTO, (String) null, values);
    }

    public void updateNotes(long id, String n) {
        ContentValues values = new ContentValues();
        if (n == null || n.isEmpty()) {
            values.putNull("NOTES");
        } else {
            values.put("NOTES", n);
        }
        this.database.update(CosplannerSQLiteHelper.TABLE_CP_PHOTO, values, "_id = " + id, (String[]) null);
    }

    public void deletePhoto(long id) {
        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_PHOTO, "_id = " + id, (String[]) null);
    }

    public void deleteAllPhotos(long fkCos) {
        this.database.delete(CosplannerSQLiteHelper.TABLE_CP_PHOTO, "FK_COS = " + fkCos, (String[]) null);
    }

    public CPImage[] getAllPhotos(long fkCos) {
        Cursor cursor = this.database.query(CosplannerSQLiteHelper.TABLE_CP_PHOTO, this.allColumns,
                "FK_COS = " + fkCos, (String[]) null, (String) null, (String) null, (String) null);
        CPImage[] arrayPhotos = new CPImage[cursor.getCount()];
        int idx = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayPhotos[idx] = cursorToImage(cursor);
            idx++;
            cursor.moveToNext();
        }
        cursor.close();
        return arrayPhotos;
    }

    private CPImage cursorToImage(Cursor cursor) {
        CPImage image = new CPImage();
        image.setId(cursor.getLong(0));
        image.setFkCos(cursor.getLong(1));
        image.setUrlThumb(cursor.getString(2));
        image.setUrlImage(cursor.getString(3));
        image.setOrder(cursor.getInt(4));
        if (cursor.getString(5) == null || cursor.getString(5).isEmpty()) {
            image.setNotes("");
        } else {
            image.setNotes(cursor.getString(5));
        }
        return image;
    }
}
