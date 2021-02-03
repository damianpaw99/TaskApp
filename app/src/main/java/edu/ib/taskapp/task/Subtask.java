package edu.ib.taskapp.task;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Subtask implements Comparable<Subtask>, Serializable {
    String name;
    protected LocalDate date;
    protected boolean finished = false;

    public Subtask() {
    }

    public Subtask(String name, LocalDate date, boolean finished) {
        this.name = name;
        this.date = date;
        this.finished = finished;
    }

    @Override
    public int compareTo(Subtask o) {
        return o.date.compareTo(date);
    }

    public int daysLeft(){
        return (int) ChronoUnit.DAYS.between(LocalDate.now(),date);
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    public static Subtask readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,null,"task");
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
        return new Subtask(name,LocalDate.parse(date),Boolean.parseBoolean(finished));
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String out="";
        if(parser.next()==XmlPullParser.TEXT){
            out=parser.getText();
            parser.nextTag();
        }
        return out;
    }
    public boolean isAfterDate(){
        return date.isBefore(LocalDate.now());
    }


}
