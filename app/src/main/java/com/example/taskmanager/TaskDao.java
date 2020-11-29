package com.example.taskmanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

public interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY text")
    public List<Task> getSubjects();

   // @Query("SELECT * FROM subjects ORDER BY updated DESC")
    //public List<Task> getSubjectsNewerFirst();

   // @Query("SELECT * FROM subjects ORDER BY updated ASC")
   // public List<Task> getSubjectsOlderFirst();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insertSubject(Task task);

    @Update
    public void updateSubject(Task task);

    @Delete
    public void deleteSubject(Task task);
}
