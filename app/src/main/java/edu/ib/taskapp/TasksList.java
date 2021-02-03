package edu.ib.taskapp;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ib.taskapp.task.Subtask;
import edu.ib.taskapp.task.Task;

public class TasksList implements Serializable {
    private List<Task> taskList;
    private File file;


    public TasksList(){
        taskList=new ArrayList<>();
    }

    public TasksList(File file){
        this();
        this.file=file;
    }

    public TasksList(File file, List<Task> taskList){
        this(file);
        this.taskList=taskList;
    }

    public Task getTask(int index){
        return taskList.get(index);
    }
    public int size(){
        return taskList.size();
    }

    public void clear(){
        taskList.clear();
    }

    public void addAll(TasksList list){
        taskList.addAll(list.getTaskList());
    }

    public boolean saveToFile(){

        try(FileOutputStream os=new FileOutputStream(file)) {
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(os,"UTF-8");
            serializer.startDocument("UTF-8",true);
            serializer.startTag(null,"tasksList");
            for(int i=0;i<taskList.size();i++) {

                serializer.startTag(null, "Task");

                serializer.startTag(null, "Name");
                serializer.text(taskList.get(i).getName());
                serializer.endTag(null,"Name");

                serializer.startTag(null,"Date");
                serializer.text(taskList.get(i).getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                serializer.endTag(null,"Date");

                serializer.startTag(null,"Finished");
                serializer.text(String.valueOf(taskList.get(i).isFinished()));
                serializer.endTag(null,"Finished");

                serializer.startTag(null,"Notification");
                serializer.text(String.valueOf(taskList.get(i).isNotification()));
                serializer.endTag(null,"Notification");

                serializer.startTag(null,"Priority");
                serializer.text(taskList.get(i).getPriority().toString());
                serializer.endTag(null,"Priority");

                ArrayList<Subtask> subtask=(ArrayList<Subtask>)taskList.get(i).getSubtaskList();
                for(int j=0; j<subtask.size();j++){
                    serializer.startTag(null,"Subtask");
                    serializer.text(String.valueOf(taskList.get(i).getSubtaskList().get(j).getName()));
                    serializer.endTag(null,"Subtask");
                }
                serializer.endTag(null,"Task");
            }
            serializer.endTag(null,"tasksList");
            serializer.endDocument();
            os.close();
        } catch (IOException e){
            Log.e(MainActivity.LOG_TAG,e.toString());
            return false;
        }
        return true;
    }
    public boolean loadFromFile(){
        try(FileInputStream is=new FileInputStream(file)){
            XmlPullParser parser=Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is,"UTF-8");
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG,null,"tasksList");
            while(parser.next()!=XmlPullParser.END_TAG){
                taskList.add(Task.readEntry(parser));
            }
        }catch(Exception e){
            Log.e(MainActivity.LOG_TAG,e.toString());
            return false;
        }
        return true;
    }

    public Task get(int index){
        return taskList.get(index);
    }

    public int indexOf(Task task){
        return taskList.indexOf(task);
    }
    public void remove(Task task){
        taskList.remove(task);
    }

    public void remove(int index){
        taskList.remove(index);
    }
    public void add(Task task){
        taskList.add(task);
    }
    public void add(int index,Task task){
        taskList.add(index, task);
    }

    public void sort(){
        Collections.sort(taskList);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
