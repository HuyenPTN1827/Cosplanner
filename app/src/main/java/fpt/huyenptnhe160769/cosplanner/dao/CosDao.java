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
    List<Cos> getAllCos();

    @Insert
    void insert(Cos cos);

    @Update
    void update(Cos cos);

    @Delete
    void delete(Cos cos);

    @Query("SELECT * FROM Cos WHERE cid = :id")
    Cos findById(int id);

    @Query("SELECT * FROM Cos WHERE Name LIKE :name OR `Series name` LIKE :name")
    List<Cos> searchCos(String name);

    @Query("SELECT * FROM Cos ORDER BY Name")
    List<Cos> orderByName();

    @Query("SELECT * FROM Cos ORDER BY `Series name`")
    List<Cos> orderBySeries();

    @Query("SELECT * FROM Cos ORDER BY budget")
    List<Cos> orderByBudget();

    @Query("SELECT * FROM Cos ORDER BY `Init date`")
    List<Cos> orderByInitDate();

    @Query("SELECT * FROM Cos ORDER BY `Due date`")
    List<Cos> orderByDueDate();

    @Query("SELECT * FROM Cos WHERE `Is Completed` = :completed")
    List<Cos> getCompletedCos(boolean completed);
}
