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

/**
 * Class storing Task in list
 */
public class TasksList implements Serializable {
    /**
     * List of Tasks
     */
    private List<Task> taskList;
    /**
     * File path to save list of tasks
     */
    private File file;

    /**
     * Default constructor creating new empty task list
     */
    public TasksList(){
        taskList=new ArrayList<>();
    }

    /**
     * Constructor creating new empty task list with filepath to save it
     * @param file File path used to save tasks
     */
    public TasksList(File file){
        this();
        this.file=file;
    }

    /**
     * Constructor creating task list using List with filepath to save it
     * @param file File path used to save tasks
     * @param taskList List of tasks
     */
    public TasksList(File file, List<Task> taskList){
        this(file);
        this.taskList=taskList;
    }


    /**
     * Method returning size of tasks list
     * @return Size of list
     */
    public int size(){
        return taskList.size();
    }

    /**
     * Method clearing list
     */
    public void clear(){
        taskList.clear();
    }

    /**
     * Method adding all task from another TasksList
     * @param list Another TaskList object with Tasks to add
     */
    public void addAll(TasksList list){
        taskList.addAll(list.getTaskList());
    }

    /**
     * Method saving Tasks from the list to file
     * @return Boolean value, showing if saving to file was successful
     */
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
                    serializer.startTag(null,"Name");
                    serializer.text(taskList.get(i).getSubtaskList().get(j).getName());
                    serializer.endTag(null,"Name");
                    serializer.startTag(null,"Date");
                    serializer.text(taskList.get(i).getSubtaskList().get(j).getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    serializer.endTag(null,"Date");
                    serializer.startTag(null,"Finished");
                    serializer.text(String.valueOf(taskList.get(i).getSubtaskList().get(j).isFinished()));
                    serializer.endTag(null, "Finished");
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

    /**
     * Method loading tasks from file
     * @return Boolean value, showing if loading from file was successful
     */
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

    /**
     * Task getter
     * @param index Index of task from list
     * @return Task from list
     */
    public Task get(int index){
        return taskList.get(index);
    }

    /**
     * Method returning index of Task from list
     * @param task Task from list
     * @return Index of task
     */
    public int indexOf(Task task){
        return taskList.indexOf(task);
    }

    /**
     * Method removing task from list
     * @param task Task to remove
     */
    public void remove(Task task){
        taskList.remove(task);
    }

    /**
     * Method removing task with certain index from list
     * @param index Index of task to remove
     */
    public void remove(int index){
        taskList.remove(index);
    }

    /**
     * Method adding task to list
     * @param task Task to add
     */
    public void add(Task task){
        taskList.add(task);
    }

    /**
     * Method adding task to list on certain index
     * @param index Index of new task
     * @param task Task to add
     */
    public void add(int index,Task task){
        taskList.add(index, task);
    }

    /**
     * Method sorting task in list
     */
    public void sort(){
        Collections.sort(taskList);
    }

    /**
     * Method returning tasks list
     * @return Tasks list
     */
    public List<Task> getTaskList() {
        return taskList;
    }

    /**
     * Method setting taskList
     * @param taskList Task list to set
     */
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
