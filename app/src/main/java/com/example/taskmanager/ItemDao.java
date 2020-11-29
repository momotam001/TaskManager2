package com.example.taskmanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items WHERE id = :id")
    public Items getItem(long id);

    @Query("SELECT * FROM items WHERE task = :task")
    public List<Items> getQuestions(String subject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertQuestion(Question question);

    @Update
    public void updateQuestion(Question question);

    @Delete
    public void deleteQuestion(Question question);
}
