package com.jhartmayer.todolist;

public class Task {
    private String title = "";
    private String description = "";
    private String dueDate = "";
    private String adtnlDescription = "";
    protected long id = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAdtnlDescription() {
        return adtnlDescription;
    }

    public void setAdtnlDescription(String adtnlDescription) {
        this.adtnlDescription = adtnlDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        return getTitle();
    }
}
