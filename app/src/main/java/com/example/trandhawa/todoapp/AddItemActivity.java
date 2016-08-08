package com.example.trandhawa.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {

    private EditText etTitle;
    private DatePicker dpDeadline;
    private EditText etNotes;
    private Spinner spStatus;
    private Spinner spPriority;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etTitle = (EditText) findViewById(R.id.etTitle);
                String title = etTitle.getText().toString();
                dpDeadline = (DatePicker) findViewById(R.id.dpDeadline);
//                String deadline = DateFormat.getDateInstance().format(dpDeadline.getCalendarView().getDate());

                int   day  = dpDeadline.getDayOfMonth();
                int   month= dpDeadline.getMonth();
                int   year = dpDeadline.getYear();

                SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String deadline = mySimpleDateFormat.format(new Date(year, month, day));

                etNotes = (EditText) findViewById(R.id.etNotes);
                String notes = etNotes.getText().toString();
                spStatus = (Spinner) findViewById(R.id.spStatus);
                String status = spStatus.getSelectedItem().toString();
                spPriority = (Spinner) findViewById(R.id.spPriority);
                String priority = spPriority.getSelectedItem().toString();

                Intent updatedItem = new Intent(AddItemActivity.this, MainActivity.class);
                updatedItem.putExtra("Title", title);
                updatedItem.putExtra("Deadline" , deadline);
                updatedItem.putExtra("Notes", notes);
                updatedItem.putExtra("Status", status);
                updatedItem.putExtra("Priority", priority);

                setResult(RESULT_OK, updatedItem);

                ToDoItem newItem = new ToDoItem();
                newItem.setDeadline(new Date(year, month, day));
                newItem.setTitle(title);
                newItem.setStatus(status);
                newItem.setPriority(priority);
                newItem.setNotes(notes);

                SQLiteDatabaseHandler.getInstance(getApplicationContext()).addItem(newItem);

                AddItemActivity.this.finish();
            }
        });
    }
}
