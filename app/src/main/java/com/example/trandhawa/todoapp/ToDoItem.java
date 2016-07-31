package com.example.trandhawa.todoapp;

import java.util.Date;

/**
 * Created by trandhawa on 7/28/16.
 */
public class ToDoItem {

    private String title;
    private Date deadline;
    private String notes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
