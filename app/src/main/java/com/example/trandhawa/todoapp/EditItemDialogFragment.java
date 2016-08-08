package com.example.trandhawa.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trandhawa on 8/4/16.
 */
public class EditItemDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText etEditTitle;
    private DatePicker dpEditDatePicker;
    private EditText etEditNotes;
    private Spinner spEditStatus;
    private Spinner spEditPriority;
    private Button btnEditSave;
    private int index;

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditItemDialogListener activity = (EditItemDialogListener) getActivity();
            SimpleDateFormat sdFormatter = new SimpleDateFormat("MM/dd/yyyy");

            int year = dpEditDatePicker.getYear();
            int month = dpEditDatePicker.getMonth();
            int date = dpEditDatePicker.getDayOfMonth();

            Date deadlineDate = new Date();
            deadlineDate.setDate(date);
            deadlineDate.setYear(year);
            deadlineDate.setMonth(month);

            String deadlineStr = sdFormatter.format(deadlineDate);
            activity.onFinishEditDialog(etEditTitle.getText().toString(), deadlineStr, etEditNotes.getText().toString(), spEditStatus.getSelectedItem().toString(),spEditPriority.getSelectedItem().toString(), index);
            this.dismiss();
            return true;
        }
        return false;
    }

    public interface EditItemDialogListener{
        void onFinishEditDialog(String title, String deadline, String notes, String status, String priority, int index);
    }

    public EditItemDialogFragment() {
    }

    public static EditItemDialogFragment newInstance(String title, Date deadline, String status, String priority, String notes, int index){

        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("Title",title);
        SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String deadlineStr = mySimpleDateFormat.format(deadline);
        args.putString("Deadline", deadlineStr);
        args.putString("Status", status);
        args.putString("Priority", priority);
        args.putString("Notes", notes);
        args.putInt("Index", index);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_item, container);

        etEditTitle = (EditText) view.findViewById(R.id.etEditTitle);
        dpEditDatePicker = (DatePicker) view.findViewById(R.id.dpEditDeadline);
        etEditNotes = (EditText) view.findViewById(R.id.etEditNotes);
        spEditStatus = (Spinner) view.findViewById(R.id.spEditStatus);
        spEditPriority = (Spinner) view.findViewById(R.id.spEditPriority);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString("Title");
        String notes = getArguments().getString("Notes");
        String status = getArguments().getString("Status");
        String priority = getArguments().getString("Priority");
        String deadline = getArguments().getString("Deadline");
        index = getArguments().getInt("Index");
        SimpleDateFormat sdfDeadline = new SimpleDateFormat("MM/dd/yyyy");
        Date deadlineDate = null;
        try{
            deadlineDate = sdfDeadline.parse(deadline);
        } catch(Exception e){
            Log.d("EditItemDialogFragment", "Exception while updating DatePicker " + e);
        }

        int year = deadlineDate.getYear();
        int month = deadlineDate.getMonth();
        int date = deadlineDate.getDate();

        dpEditDatePicker.updateDate(year,month,date);

        etEditTitle.setText(title);
        etEditNotes.setText(notes);
        spEditStatus.setSelection(getIndex(spEditStatus, status));
        spEditPriority.setSelection(getIndex(spEditPriority, priority));

        etEditTitle.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnEditSave = (Button) view.findViewById(R.id.btnEditSave);
        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditItemDialogListener activity = (EditItemDialogListener) getActivity();
                SimpleDateFormat sdFormatter = new SimpleDateFormat("MM/dd/yyyy");

                int year = dpEditDatePicker.getYear();
                int month = dpEditDatePicker.getMonth();
                int date = dpEditDatePicker.getDayOfMonth();

                Date deadlineDate = new Date();
                deadlineDate.setDate(date);
                deadlineDate.setYear(year);
                deadlineDate.setMonth(month);

                String deadlineStr = sdFormatter.format(deadlineDate);
                activity.onFinishEditDialog(etEditTitle.getText().toString(), deadlineStr, etEditNotes.getText().toString(), spEditStatus.getSelectedItem().toString(),spEditPriority.getSelectedItem().toString(), index);
                getDialog().dismiss();
            }
        });
    }

    private int getIndex(Spinner spinner, String selection){

        int index = 0;
        for(int i=0; i<spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(selection)){
                index = i;
            }
        }

        return index;
    }


}
