package edu.ib.taskapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Task {
    private String name;
    private LocalDate date;
    private LocalTime time;
    private boolean finished=false;
    private Priority priority;
    private boolean notification;
    public enum Priority{
        High,
        Normal,
        Low
    }

    /**
     * Default Task constructor
     */
    public Task(){
        name="Task";
        date=LocalDate.now();
        time=LocalTime.now();
        notification=false;

    }
    public Task(String name, LocalDate date, LocalTime time, boolean notification) {
        this.name = name;
        this.date = date;
        this.time = time;
        priority=Priority.Normal;
        this.notification=notification;
    }
    public Task(String name, LocalDate date, LocalTime time,boolean notification, Priority priority) {
        this(name, date, time, notification);
        this.priority = priority;
    }

    public boolean isAfterDateTime(){
        LocalDateTime d=LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),time.getHour(),time.getMinute(),time.getSecond());
        return d.isAfter(LocalDateTime.now());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", finished=" + finished +
                ", priority=" + priority +
                '}';
    }
}
