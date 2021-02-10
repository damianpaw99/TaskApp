package edu.ib.taskapp.task;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class describing task extending Subtask
 */
public class Task extends Subtask{
    /**
     * Priority of task
     */
    private Priority priority;
    /**
     * Boolean value describing if made a notification for task (not implemented)
     */
    private boolean notification;
    /**
     * List of Subtasks
     */
    private List<Subtask> subtaskList=new ArrayList<>();

    /**
     * Priority enum class
     */
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

    /**
     * Constructor with some parameters
     * @param name Name of task
     * @param date Date of deadline
     * @param notification Boolean notification value
     */
    public Task(String name, LocalDate date, boolean notification) {
        this.name = name;
        this.date = date;
        priority=Priority.Normal;
        this.notification=notification;
    }
    /**
     * Full task constructor
     * @param name Name of task
     * @param date Date of deadline
     * @param notification Boolean notification value
     * @param priority Priority of task
     * @param finished Boolean value if task is finished
     */
    public Task(String name, LocalDate date, boolean notification, Priority priority,boolean finished,List<Subtask> subtasks) {
        this(name, date, finished);
        this.notification=notification;
        this.priority = priority;
        subtaskList=subtasks;
    }

    /**
     * Method adding subtask
     * @param subtask Subtask to add
     */
    public void addSubtask(Subtask subtask){
        subtaskList.add(subtask);
    }

    /**
     * Method getting subtask list
     * @return
     */
    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList){
        this.subtaskList=subtaskList;
    }

    /**
     * Method getting priority
     * @return Priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Method setting Priority
     * @param priority Priority
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Method returning boolean value if notification is set
     * @return Boolean value if notification is set
     */
    public boolean isNotification() {
        return notification;
    }

    /**
     * Method setting boolean value if notification is set
     * @param notification Boolean value if notification is set
     */
    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    /**
     * Method reading Task values from file
     * @param parser XMLPullParser
     * @return Task
     * @throws IOException
     * @throws XmlPullParserException
     */
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

    /**
     * Method reading  String value of tags from xml file
     * @param parser XMLPullParser
     * @return String value
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String out="";
        if(parser.next()==XmlPullParser.TEXT){
            out=parser.getText();
            parser.nextTag();
        }
        return out;
    }

    /**
     * Overridden method toString
     * @return Information about task
     */
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", finished=" + finished +
                ", priority=" + priority +
                '}';
    }

    /**
     * Method checking if values (without subtasks) are the same in this and another task
     * @param task Task to compare
     * @return Boolean value of comparison
     */
    public boolean isTheSame(Task task){
        boolean test1=name.equals(task.name) && ChronoUnit.DAYS.between(date, task.date) == 0
                && priority.equals(task.priority) && notification == task.notification
                && finished == task.finished;
        if(!test1) return false;
        if(subtaskList.size()!=task.subtaskList.size()) return false;

        return true;
    }

}
