package fpt.huyenptnhe160769.cosplanner.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
    @ForeignKey(entity = Cos.class, parentColumns = "cid", childColumns = "cid", onDelete = ForeignKey.CASCADE)
})
public class Element {
    @PrimaryKey(autoGenerate = true)
    public int eid;
    public int cid;
    @ColumnInfo(name = "Name")
    public String name;
    @ColumnInfo(name = "Notes")
    public String note;
    @ColumnInfo(name = "Cost")
    public double cost;
    @ColumnInfo(name = "Picture URL")
    public String pictureURL;
    @ColumnInfo(name = "Is Priority", defaultValue = "false")
    public boolean isPriority;
    @ColumnInfo(name = "Is Complete", defaultValue = "false")
    public boolean isComplete;
}
