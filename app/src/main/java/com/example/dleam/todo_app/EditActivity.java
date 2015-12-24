package com.example.dleam.todo_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    private TodoItem mItem;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            mEditText = (EditText) findViewById(R.id.editText);
            mItem = (TodoItem) extras.getSerializable("item");
            mEditText.setText(mItem.content);
            mEditText.setSelection(mEditText.getText().length());
        }
    }

    public void editTodo(View view) {
        Bundle extras = new Bundle();
        Intent intent = new Intent();
        mItem.content = mEditText.getText().toString();
        extras.putSerializable("item", mItem);

        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }
}
