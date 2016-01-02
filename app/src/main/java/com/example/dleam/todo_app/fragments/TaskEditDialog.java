package com.example.dleam.todo_app.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dleam.todo_app.R;
import com.example.dleam.todo_app.models.TodoTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by DevinL on 12/29/2015.
 */
public class TaskEditDialog extends DialogFragment implements View.OnFocusChangeListener{
    private EditText mTaskTitle;
    private EditText mTaskDueDate;
    private EditText mTaskNotes;
    private Spinner mTaskPriority;
    private Spinner mTaskStatus;
    private Button mTaskSave;
    private TodoTask mTask;
    private ArrayAdapter<CharSequence> mPriorityAdapter;
    private ArrayAdapter<CharSequence> mStatusAdapter;

    private SimpleDateFormat mDateFormatter;
    private DatePickerDialog mDueDatePickerDialog;

    public TaskEditDialog() {}

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) mDueDatePickerDialog.show();
    }

    public interface TaskEditDialogListener {
        void onFinishEditDialog(TodoTask task);
    }

    public static TaskEditDialog newInstance(String title) {
        Bundle args = new Bundle();
        TaskEditDialog fragment = new TaskEditDialog();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public static TaskEditDialog newInstance(String title, TodoTask task) {
        Bundle args = new Bundle();
        TaskEditDialog fragment = new TaskEditDialog();
        args.putString("title", title);
        args.putSerializable("task", task);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_task, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(getArguments().getString("title"));

        initializeVariables(view);

        mTaskSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskEditDialogListener listener = (TaskEditDialogListener) getActivity();
                mTask.title = mTaskTitle.getText().toString();
                mTask.priority = mTaskPriority.getSelectedItem().toString();
                mTask.dueDate = mTaskDueDate.getText().toString();
                mTask.notes = mTaskNotes.getText().toString();
                mTask.status = mTaskStatus.getSelectedItem().toString();

                listener.onFinishEditDialog(mTask);
                dismiss();
            }
        });

        mTaskDueDate.setOnFocusChangeListener(this);

        Calendar newCalendar = Calendar.getInstance();
        mDueDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mTaskDueDate.setText(mDateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void initializeVariables(View view) {
        mPriorityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.priority_array, android.R.layout.simple_spinner_dropdown_item);
        mStatusAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.status_array, android.R.layout.simple_spinner_dropdown_item);

        mTaskTitle = (EditText) view.findViewById(R.id.edit_task_title);
        mTaskDueDate = (EditText) view.findViewById(R.id.edit_task_due_date);
        mTaskPriority = (Spinner) view.findViewById(R.id.edit_task_priority);
        mTaskStatus = (Spinner) view.findViewById(R.id.edit_task_status);
        mTaskNotes = (EditText) view.findViewById(R.id.edit_task_notes);
        mTaskSave = (Button) view.findViewById(R.id.task_done);

        mTaskPriority.setAdapter(mPriorityAdapter);
        mTaskStatus.setAdapter(mStatusAdapter);

        mDateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        mTaskDueDate.setInputType(InputType.TYPE_NULL);

        if((mTask = (TodoTask) getArguments().getSerializable("task")) == null) {
            mTask = new TodoTask();
        } else {
            setTaskValues();
        }
    }

    private void setTaskValues() {
        mTaskTitle.setText(mTask.title);
        mTaskDueDate.setText(mTask.dueDate);
        mTaskNotes.setText(mTask.notes);
        mTaskPriority.setSelection(mPriorityAdapter.getPosition(mTask.priority));
        mTaskStatus.setSelection(mStatusAdapter.getPosition(mTask.status));
    }
}

