package com.example.trandhawa.todoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trandhawa on 8/4/16.
 */
public class EditItemDialogFragment extends DialogFragment {

    private EditText etEditTitle;
    private DatePicker dpEditDatePicker;
    private EditText etEditNotes;
    private Spinner spEditStatus;
    private Spinner spEditPriority;

    public EditItemDialogFragment() {
    }

    public static EditItemDialogFragment newInstance(String title, Date deadline, String status, String priority, String notes){

        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("Title",title);

        SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        String deadlineStr = mySimpleDateFormat.format(deadline);
        args.putString("Deadline", deadlineStr);

        args.putString("Status", status);
        args.putString("Priority", priority);
        args.putString("Notes", notes);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEditTitle = (EditText) view.findViewById(R.id.etEditTitle);
        dpEditDatePicker = (DatePicker) view.findViewById(R.id.dpEditDeadline);
        etEditNotes = (EditText) view.findViewById(R.id.etEditNotes);
        spEditStatus = (Spinner) view.findViewById(R.id.spEditStatus);
        spEditPriority = (Spinner) view.findViewById(R.id.spEditPriority); 

        String title = getArguments().getString("Title");
        String notes = getArguments().getString("Notes");
        String status = getArguments().getString("Status");
        String priority = getArguments().getString("Priority");


        etEditTitle.setText(title);
        etEditNotes.setText(notes);
        spEditStatus.setSelection(getIndex(spEditStatus, status));
        spEditPriority.setSelection(getIndex(spEditPriority, priority));

        etEditTitle.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
