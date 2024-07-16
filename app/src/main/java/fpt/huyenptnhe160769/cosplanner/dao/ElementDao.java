package fpt.huyenptnhe160769.cosplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import fpt.huyenptnhe160769.cosplanner.models.Element;

@Dao
public interface ElementDao {

    @Query("SELECT * FROM Element WHERE cid = :cid")
    List<Element> getByCosId(int cid);

    @Insert
    void insert(Element element);

    @Update
    void update(Element element);

    @Delete
    void delete(Element element);

    @Query("SELECT * FROM Element WHERE eid = :id")
    Element findById(int id);

    @Query("SELECT * FROM Element WHERE Name LIKE '%' || :name || '%'")
    List<Element> findByName(String name);
}
