package edu.ib.taskapp.task;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Task extends Subtask{

    private Priority priority;
    private boolean notification;
    private List<Subtask> subtaskList=new ArrayList<>();

    public enum Priority{
        High,
        Normal,
        Low
    }

    /**
     * Default Task constructor
     */
    public Task(){

    }
    public Task(String name, LocalDate date, boolean notification) {
        this.name = name;
        this.date = date;
        priority=Priority.Normal;
        this.notification=notification;
    }
    public Task(String name, LocalDate date, boolean notification, Priority priority,boolean finished,List<Subtask> subtasks) {
        this(name, date, finished);
        this.notification=notification;
        this.priority = priority;
        subtaskList=subtasks;
    }

    public void addSubtask(Subtask subtask){
        subtaskList.add(subtask);
    }


    public List<Subtask> getSubtaskList() {
        return subtaskList;
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

    public static Task readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        Task task=new Task();
        parser.require(XmlPullParser.START_TAG,null,"Task");
        String name=null;
        String date=null;
        String finished=null;
        String priority=null;
        String notification=null;
        ArrayList<Subtask> subtasks=new ArrayList<>();

        while(parser.next()!=XmlPullParser.END_TAG){
            if(parser.getEventType()!=XmlPullParser.START_TAG){
                continue;
            }
            String item=parser.getName();
            if(item.equals("Name")){
                parser.require(XmlPullParser.START_TAG,null,"Name");
                name=readText(parser);
                parser.require(XmlPullParser.END_TAG,null,"Name");
            } else if (item.equals("Date")){
                parser.require(XmlPullParser.START_TAG,null,"Date");
                date=readText(parser);
                parser.require(XmlPullParser.END_TAG,null,"Date");
            } else if(item.equals("Finished")){
                parser.require(XmlPullParser.START_TAG,null,"Finished");
                finished=readText(parser);
                parser.require(XmlPullParser.END_TAG,null,"Finished");
            } else if (item.equals("Notification")){
                parser.require(XmlPullParser.START_TAG,null,"Notification");
                notification=readText(parser);
                parser.require(XmlPullParser.END_TAG,null,"Notification");
            } else if (item.equals("Priority")){
                parser.require(XmlPullParser.START_TAG,null,"Priority");
                priority=readText(parser);
                parser.require(XmlPullParser.END_TAG,null,"Priority");
            } else if (item.equals("Subtask")){
                subtasks.add(Subtask.readEntry(parser));
            }
        }
        return new Task(name,LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")),Boolean.parseBoolean(notification),Priority.valueOf(priority),Boolean.parseBoolean(finished),subtasks);
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String out="";
        if(parser.next()==XmlPullParser.TEXT){
            out=parser.getText();
            parser.nextTag();
        }
        return out;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", finished=" + finished +
                ", priority=" + priority +
                '}';
    }
}
