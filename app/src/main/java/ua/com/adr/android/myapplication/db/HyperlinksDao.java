package ua.com.adr.android.myapplication.db;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface HyperlinksDao {
    @Query("SELECT * FROM Hyperlinks")
    LiveData<List<Hyperlinks>> findAll();

    @Query("SELECT * FROM Hyperlinks ORDER BY mTime DESC")
    LiveData<List<Hyperlinks>> findAllByDate();

    @Query("SELECT * FROM Hyperlinks ORDER BY mStatus ASC")
    LiveData<List<Hyperlinks>> findAllByStatus();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(List<Hyperlinks> mHyperlink);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Hyperlinks mHyperlink);

    @Update
    void update(Hyperlinks mHyperlink);

    @Query("DELETE FROM Hyperlinks WHERE mUrl = :link")
    void delete(String link);
}
