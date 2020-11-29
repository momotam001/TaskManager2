package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Task")
public class Task {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "text")
    private String mText = "";


    public Task(String text) {
        mText = text;

    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

}