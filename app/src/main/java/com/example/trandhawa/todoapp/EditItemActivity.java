package com.example.trandhawa.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItemText;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etEditItemText = (EditText) findViewById(R.id.etEditItemText);
        etEditItemText.setText(getIntent().getStringExtra("ItemTitle"));

        btnSave = (Button) findViewById(R.id.btnSaveEditItem);

        btnSave.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View v) {

                String updatedText = etEditItemText.getText().toString();
                Intent updatedItem = new Intent(EditItemActivity.this, MainActivity.class);
                updatedItem.putExtra("ItemTitle", updatedText);
                setResult(RESULT_OK, updatedItem);
                EditItemActivity.this.finish();
            }
        });

    }



}
