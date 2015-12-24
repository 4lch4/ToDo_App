package com.example.dleam.todo_app;


import java.io.Serializable;

/**
 * Created by dleam on 12/22/2015.
 */
public class ListItem implements Serializable {
    private static final long serialVersionUID = 1L;
    public String content;
    public int position;

    public ListItem(String content) {
        this.content = content;
    }

    public String getContent() { return content; }
}
