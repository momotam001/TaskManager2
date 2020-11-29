package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class TaskEditActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "com.example.taskmanager.item_id";
    public static final String EXTRA_TASK = "com.example.taskmanager.task";

    private EditText mItemText;
   // private EditText mAnswerText;

    private taskDatabase mStudyDb;
    private long mItemId;
    private Item mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkTheme = sharedPrefs.getBoolean(settingsFragment.PREFERENCE_THEME, false);
        if (darkTheme) {
            setTheme(R.style.DarkTheme);
        }
        setContentView(R.layout.activity_task_edit);

        mItemText = findViewById(R.id.itemText);
       // mAnswerText = findViewById(R.id.answerText);

        mStudyDb = taskDatabase.getInstance(getApplicationContext());

        // Get question ID from QuestionActivity
        Intent intent = getIntent();
        mItemId = intent.getLongExtra(EXTRA_QUESTION_ID, -1);

        ActionBar actionBar = getSupportActionBar();

        if (mItemId == -1) {
            // Add new question
            mItem = new Item();
            setTitle(R.string.add_item);
        }
        else {
            // Update existing question
            mItem = mStudyDb.questionDao().getQuestion(mItemId);
            mItemText.setText(mItem.getText());
            //AnswerText.setText(mQuestion.getAnswer());
            setTitle(R.string.update_item);
        }

        String subject = intent.getStringExtra(EXTRA_SUBJECT);
        mQuestion.setSubject(subject);
    }
    }

    public void saveButtonClick(View view) {


            mItem.setText(mItemText.getText().toString());
            //mQuestion.setAnswer(mAnswerText.getText().toString());

            if (mItemId == -1) {
                // New question
                mStudyDb.questionDao().insertQuestion(mItem);
            } else {
                // Existing question
                mStudyDb.questionDao().updateQuestion(mItem);
            }

            // Send back question ID
            Intent intent = new Intent();
            intent.putExtra(EXTRA_QUESTION_ID, mQuestion.getId());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
