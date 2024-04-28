package com.TasksManager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "task_title")
    private String title;
    @Column(name = "task_description")
    private String description;
    @Column(name = "task_priority")
    private int priority;
    @Column(name = "task_is_completed")
    private int isCompleted;
    @Column(name = "task_start_date")
    private java.sql.Date startDate;
    @Column(name = "task_end_date")
    private java.sql.Date endDate;

    public Task(){}

    public Task(long id,  String title, String description, int priority, int isCompleted, java.sql.Date startDate, java.sql.Date endDate) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isCompleted = isCompleted;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCompleted() {
        return isCompleted;
    }

    public void setCompleted(int completed) {
        isCompleted = completed;
    }

    public java.sql.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", isCompleted=" + isCompleted +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
