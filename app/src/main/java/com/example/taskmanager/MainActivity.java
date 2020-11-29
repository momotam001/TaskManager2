package com.example.taskmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TaskDialogFragment.OnTaskEnteredListener{


    private Task mSelectedTask;

    private int mSelectedSubjectPosition = RecyclerView.NO_POSITION;
    private ActionMode mActionMode = null;

    private taskDatabase mTaskDb;
    private SubjectAdapter mSubjectAdapter;
    private RecyclerView mRecyclerView;
    private int[] mTaskColors;


    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;

    private TaskManager mTaskManager;
    private EditText mItemEditText;
    private TextView mItemListTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkTheme = mSharedPrefs.getBoolean(settingsFragment.PREFERENCE_THEME, false);
        if (mDarkTheme) {
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskColors = getResources().getIntArray(R.array.subjectColors);

        // Singleton
        mTaskDb = taskDatabase.getInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.itemsRecyclerView);

        // Create 2 grid layout columns


        // Shows the available subjects
        mSubjectAdapter = new SubjectAdapter(loadSubjects());
        mRecyclerView.setAdapter(mSubjectAdapter);
    }

      //  mItemEditText = findViewById(R.id.taskText);
        //mItemListTextView = findViewById(R.id.itemList);

      //  mTaskManager = new TaskManager(this);


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, settingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean darkTheme = mSharedPrefs.getBoolean(settingsFragment.PREFERENCE_THEME, false);
        if (darkTheme != mDarkTheme) {
            recreate();
        }


        // Load subjects here in case settings changed
        mSubjectAdapter = new SubjectAdapter(loadTasks());
        mRecyclerView.setAdapter(mSubjectAdapter);
    }

    private List<Task> loadTasks() {
        String order = mSharedPrefs.getString(settingsFragment.PREFERENCE_SUBJECT_ORDER, "1");
        switch (Integer.parseInt(order)) {
            case 0:
                return mStudyDb.subjectDao().getSubjects();
            case 1:
                return mStudyDb.subjectDao().getSubjectsNewerFirst();
            default:
                return mStudyDb.subjectDao().getSubjectsOlderFirst();
        }
    }

    @Override
    public void onTaskEntered(String task) {
        // Returns subject entered in the SubjectDialogFragment dialog

            if (task.length() > 0) {
                Task sub = new Task(task);
                if (!mTaskDb.subjectDao().getSubjects().contains(sub)) {
                    mTaskDb.subjectDao().insertSubject(sub);
                    // TODO: add subject to RecyclerView
                    Toast.makeText(this, "Added " + task, Toast.LENGTH_SHORT).show();
                } else {
                    String message = getResources().getString(R.string.task_exists, task);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }

    public void addButtonClick(View view) {

        // Ignore any leading or trailing spaces
       // String item = mItemEditText.getText().toString().trim();

        // Clear the EditText so it's ready for another item
        //mItemEditText.setText("");

        // Add the item to the list and display it
        FragmentManager manager = getSupportFragmentManager();
        TaskDialogFragment dialog = new TaskDialogFragment();
        dialog.show(manager, "subjectDialog");
        }

    private class SubjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {


        private Task mTask;
        private TextView mTextView;

        public SubjectHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTextView = itemView.findViewById(R.id.itemsTextView);
        }

        public void bind(Task task, int position) {
            mTask = task;
            mTextView.setText(task.getText());

            if (mSelectedSubjectPosition == position) {
                // Make selected subject stand out
                mTextView.setBackgroundColor(Color.RED);
            } else {

                // Make the background color dependent on the length of the subject string
                int colorIndex = task.getText().length() % mTaskColors.length;
                mTextView.setBackgroundColor(mTaskColors[colorIndex]);
            }
        }
        @Override
        public boolean onLongClick(View view) {
            if (mActionMode != null) {
                return false;
            }

            mSelectedTask = mTask;
            mSelectedSubjectPosition = getAdapterPosition();

            // Re-bind the selected item
            mSubjectAdapter.notifyItemChanged(mSelectedSubjectPosition);

            // Show the CAB
            mActionMode = MainActivity.this.startActionMode(mActionModeCallback);

            return true;
        }



        @Override
        public void onClick(View view) {
            // Start QuestionActivity, indicating what subject was clicked
            Intent intent = new Intent(MainActivity.this, TaskEditActivity.class);
            intent.putExtra(TaskEditActivity.EXTRA_SUBJECT, mTask.getText());
            startActivity(intent);
        }
    }

    private class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder> {

        private List<Task> mTaskList;

        public SubjectAdapter(List<Task> subjects) {

        }

        @Override
        public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new SubjectHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SubjectHolder holder, int position) {
            holder.bind(mTaskList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mTaskList.size();
        }
    }
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Provide context menu for CAB
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // Process action item selection
            switch (item.getItemId()) {
                case R.id.delete:
                    // Delete from the database and remove from the RecyclerView
                    mStudyDb.subjectDao().deleteSubject(mSelectedTask);
                    //    mSubjectAdapter.deleteSubject(mSelectedSubject);

                    // Close the CAB
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;

            // CAB closing, need to deselect item if not deleted
            mSubjectAdapter.notifyItemChanged(mSelectedSubjectPosition);
            mSelectedSubjectPosition = RecyclerView.NO_POSITION;
        }

    };



        //private void displayList() {

        // Display a numbered list of items
        //StringBuffer itemText = new StringBuffer();
        //String[] items = mTaskManager.getItems();
        //for (int i = 0; i < items.length; i++) {
        //    itemText.append((i + 1) + ". " + items[i] + "\n");
        //}

       // mItemListTextView.setText(itemText);
   // }






    }
