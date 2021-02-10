package edu.ib.taskapp.task;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Class describing Subtask
 */
public class Subtask implements Comparable<Subtask>, Serializable {
    /**
     * Name of task
     */
    String name;
    /**
     * Date of deadline to finish task
     */
    protected LocalDate date;
    /**
     * Boolean value if task is finished
     */
    protected boolean finished = false;

    /**
     * Default constructor
     */
    public Subtask() {
    }

    /**
     * Full Subtask constructor
     * @param name Name of subtask
     * @param date Deadline of task
     * @param finished Boolean with information if subtask is finished
     */
    public Subtask(String name, LocalDate date, boolean finished) {
        this.name = name;
        this.date = date;
        this.finished = finished;
    }

    /**
     * Overridden method compering two subtasks
     * @param o Subtask to be compered with
     * @return Integer value of comparison
     */
    @Override
    public int compareTo(Subtask o) {
        return o.date.compareTo(date);
    }

    /**
     * Days left to deadline of subtask
     * @return
     */
    public int daysLeft(){
        return (int) ChronoUnit.DAYS.between(LocalDate.now(),date);
    }

    /**
     * Name getter
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Date of deadline getter
     * @return Date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Date setter
     * @param date Date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Method returning value if task is finished
     * @return Value if task is finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Method setting value of finishing of task
     * @param finished Value of finishing of task
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Method loading subtask from file
     * @param parser XMLPullParser
     * @return Loaded subtask
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static Subtask readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,null,"Subtask");
        String name=null;
        String date=null;
        String finished=null;
        while(parser.next()!=XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String item = parser.getName();
            if (item.equals("Name")) {
                parser.require(XmlPullParser.START_TAG, null, "Name");
                name = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "Name");
            } else if (item.equals("Date")) {
                parser.require(XmlPullParser.START_TAG, null, "Date");
                date = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "Date");
            } else if (item.equals("Finished")) {
                parser.require(XmlPullParser.START_TAG, null, "Finished");
                finished = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "Finished");
            }
        }
        return new Subtask(name,LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")),Boolean.parseBoolean(finished));
    }

    /**
     * Method reading value of tag in xml file
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
     * Method returning boolean value if task is after deadline
     * @return Boolean value if task is after deadline
     */
    public boolean isAfterDate(){
        return date.isBefore(LocalDate.now());
    }


}
