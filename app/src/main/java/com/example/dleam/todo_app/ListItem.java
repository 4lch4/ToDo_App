package com.example.dleam.todo_app;

/**
 * Created by dleam on 12/22/2015.
 */
public class ListItem implements Comparable<ListItem> {
    private static final long serialVersionUID = 1L;
    public String content;

    public ListItem(String contentIn) {
        this.content = contentIn;
    }

    public String getContent() { return content; }

    // Makes it so the Entry can be sorted by title
    public int compareTo(ListItem item) {
        return content.compareTo(item.content);
    }
}
