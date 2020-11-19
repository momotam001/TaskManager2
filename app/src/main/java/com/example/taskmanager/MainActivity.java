package com.example.taskmanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements SubjectDialogFragment.OnSubjectEnteredListener{

    private TaskManager mTaskManager;
    private EditText mItemEditText;
    private TextView mItemListTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mItemEditText = findViewById(R.id.taskText);
        mItemListTextView = findViewById(R.id.itemList);

        mTaskManager = new TaskManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            // Attempt to load a previously saved list
            mTaskManager.readFromFile();
            displayList();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            // Save list for later
            mTaskManager.saveToFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onTaskEntered(String task) {
        // Returns subject entered in the SubjectDialogFragment dialog
        if (task.length() > 0) {
                mTaskManager.addItem(task);
                displayList();

                Toast.makeText(this, "Added " + task, Toast.LENGTH_SHORT).show();
            } else {
                String message = getResources().getString(R.string.subject_exists, task);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }


    public void addButtonClick(View view) {

        // Ignore any leading or trailing spaces
       // String item = mItemEditText.getText().toString().trim();

        // Clear the EditText so it's ready for another item
        //mItemEditText.setText("");

        // Add the item to the list and display it
        FragmentManager manager = getSupportFragmentManager();
        SubjectDialogFragment dialog = new SubjectDialogFragment();
        dialog.show(manager, "subjectDialog");
        }


    private void displayList() {

        // Display a numbered list of items
        StringBuffer itemText = new StringBuffer();
        String[] items = mTaskManager.getItems();
        for (int i = 0; i < items.length; i++) {
            itemText.append((i + 1) + ". " + items[i] + "\n");
        }

        mItemListTextView.setText(itemText);
    }






    }
