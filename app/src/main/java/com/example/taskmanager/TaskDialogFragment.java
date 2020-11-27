package com.example.taskmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TaskDialogFragment extends DialogFragment {

    // Host activity must implement
    public interface OnTaskEnteredListener {
        void onTaskEntered(String subject);
    }

    private OnTaskEnteredListener mListener;

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        final EditText taskEditText = new EditText(getActivity());
        taskEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        taskEditText.setMaxLines(1);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.task)
                .setView(taskEditText)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String subject = taskEditText.getText().toString();
                        mListener.onTaskEntered(subject.trim());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnTaskEnteredListener) context;
    }
}