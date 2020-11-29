package com.example.taskmanager;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class, Items.class}, version = 1)
public abstract class taskDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "study2.db";

    private static taskDatabase mtaskDatabase;

    // Singleton
    public static taskDatabase getInstance(Context context) {
        if (mtaskDatabase == null) {
            mtaskDatabase = Room.databaseBuilder(context, taskDatabase.class,
                    DATABASE_NAME).allowMainThreadQueries().build();
        }
        return mtaskDatabase;
    }

   // public abstract QuestionDao questionDao();
   // public abstract SubjectDao subjectDao();
}