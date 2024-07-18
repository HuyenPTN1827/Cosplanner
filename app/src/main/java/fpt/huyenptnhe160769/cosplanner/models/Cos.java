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
    public double budget = 0.0d;
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

    public Cos() {
    }

    public Cos(String name, String series) {
        this.name = name;
        this.series = series;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public long getInitDate() {
        return initDate;
    }

    public void setInitDate(long initDate) {
        this.initDate = initDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
