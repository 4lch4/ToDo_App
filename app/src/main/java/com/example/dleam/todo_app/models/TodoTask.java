package com.example.dleam.todo_app.models;


import java.io.Serializable;

/**
 * Created by dleam on 12/22/2015.
 */
public class TodoTask implements Serializable {
    private static final long serialVersionUID = 1L;
    public String title;
    public String priority;
    public String dueDate;
    public String notes;
    public String status;
    public int position;
}
