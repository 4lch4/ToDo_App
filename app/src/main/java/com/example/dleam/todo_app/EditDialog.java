package com.example.dleam.todo_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by dleam on 12/25/2015.
 */
public class EditDialog extends DialogFragment implements TextView.OnEditorActionListener{
    private EditText mEditText;
    private int mPosition;

    public EditDialog() {}

    public static EditDialog newInstance(String title, TodoItem item) {
        EditDialog fragment = new EditDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("itemText", item.content);
        args.putInt("position", item.position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            TodoItem item = new TodoItem();
            item.content = mEditText.getText().toString();
            item.position = mPosition;
            EditDialogListener listener = (EditDialogListener) getActivity();
            listener.onFinishEditDialog(item);
            dismiss();
            return true;
        }
        return false;

    }

    public interface EditDialogListener {
        void onFinishEditDialog(TodoItem item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = (EditText) view.findViewById(R.id.todo_edit_text);

        String title = getArguments().getString("title", "Edit Item");
        String itemText = getArguments().getString("itemText");
        mPosition = getArguments().getInt("position");
        getDialog().setTitle(title);

        mEditText.setText(itemText);
        mEditText.requestFocus();
        mEditText.setOnEditorActionListener(this);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
