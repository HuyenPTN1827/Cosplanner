package fpt.huyenptnhe160769.cosplanner.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import fpt.huyenptnhe160769.cosplanner.services.DateConverter;


@Entity
@TypeConverters({
        DateConverter.class
})
public class Cos {
    @PrimaryKey(autoGenerate = true)
    public int cid;
    @ColumnInfo(name = "Name")
    public String name;
    @ColumnInfo(name = "Series name")
    public String series;
    @ColumnInfo(name = "Notes")
    public String note;
    @ColumnInfo(name = "Budget")
    public double budget;
    @ColumnInfo(name ="Init date")
    public long initDate;
    @ColumnInfo(name ="Due date")
    public long dueDate;
    @ColumnInfo(name ="End date")
    public long endDate;
    @ColumnInfo(name = "Is Completed", defaultValue = "false")
    public boolean isComplete;
    @ColumnInfo(name = "Picture")
    public String pictureURL;
}
