package com.example.to_dolist.Model;

public class Task {
    private int id;
    private String Taskname;
    private int starttime;
    private int deadline;
    private String status;
    private String dateadded;

    public Task() {
    }

    public Task(int id, String taskname, int starttime, int deadline, String status, String dateadded) {
        this.id = id;
        Taskname = taskname;
        this.starttime = starttime;
        this.deadline = deadline;
        this.status = status;
        this.dateadded = dateadded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskname() {
        return Taskname;
    }

    public void setTaskname(String taskname) {
        Taskname = taskname;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateadded() {
        return dateadded;
    }

    public void setDateadded(String dateadded) {
        this.dateadded = dateadded;
    }
}
