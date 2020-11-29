package com.example.taskmanager;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "items",
        foreignKeys = @ForeignKey(entity = Task.class,
                parentColumns = "text",
                childColumns = "items"))
public class Items {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "text")
    private String mText;

    // @ColumnInfo(name = "answer")
    // private String mAnswer;

    @ColumnInfo(name = "items")
    private String mItems;

    //@ColumnInfo(name = "lastCorrectTime")
    // private long  mCorrectTime;

    //public long getCorrectTime() {
    //    return mCorrectTime;
    //}
    //public void setCorrectTime(long time)
    // mCorrectTime = time;
    // }

    public String getText() {
        return mText;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setText(String text) {
        mText = text;
    }

    //public String getAnswer() {
    // return mAnswer;
    //}

    //public void setAnswer(String answer) {
    // mAnswer = answer;
    // }

    public String getItems() {

        return mItems;
    }

    public void setTasks(String subject) { mItems = Task;
    }



}


