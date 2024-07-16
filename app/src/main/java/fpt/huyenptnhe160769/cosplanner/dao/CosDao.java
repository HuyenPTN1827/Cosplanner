package fpt.huyenptnhe160769.cosplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import fpt.huyenptnhe160769.cosplanner.models.Cos;

@Dao
public interface CosDao {
    @Query("SELECT * FROM Cos")
    List<Cos> getAll();

    @Insert
    void insert(Cos cos);

    @Update
    void update(Cos cos);

    @Delete
    void delete(Cos cos);

    @Query("SELECT * FROM COS WHERE cid = :id")
    Cos findById(int id);

    @Query("SELECT * FROM COS WHERE Name LIKE '%' || :name || '%'")
    List<Cos> findByName(String name);
}
