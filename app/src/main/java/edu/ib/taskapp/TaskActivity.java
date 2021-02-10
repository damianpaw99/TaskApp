package edu.ib.taskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.ib.taskapp.recycle.SubtaskAdapter;
import edu.ib.taskapp.task.Subtask;
import edu.ib.taskapp.task.Task;

/**
 * Activity with view of one task from list of tasks
 */
public class TaskActivity extends AppCompatActivity {
    /**
     * Constant String used to move Task data to new Activity
     */
    public static final String EXTRA_TASK="taskView";
    /**
     * Constant String used to move TaskList data to new Activity
     */
    public static final String EXTRA_LIST="taskListView";

    private Task task;

    private TasksList taskList;

    private SubtaskAdapter subtaskAdapter;

    /**
     * Method loading data of task on creation of TaskActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_task);

        Intent intent=getIntent();
        task=(Task) intent.getSerializableExtra(EXTRA_TASK);
        taskList=(TasksList) intent.getSerializableExtra(EXTRA_LIST);

        TextView name=(TextView) findViewById(R.id.txtViewName);
        TextView date=(TextView) findViewById(R.id.txtViewDate);
        TextView finished=(TextView) findViewById(R.id.txtViewFinished);
        TextView daysLeft=(TextView) findViewById(R.id.txtDaysLeft);
        TextView priority=(TextView) findViewById(R.id.txtViewPriority);
        RecyclerView subtaskRV=(RecyclerView) findViewById(R.id.rvViewSubtask);

        String value=getString(R.string.nameView)+task.getName();
        name.setText(value);

        value=getString(R.string.dateView)+task.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        date.setText(value);

        value=getString(R.string.priorityView)+task.getPriority().toString();
        priority.setText(value);

        value=getString(R.string.daysLeftView)+task.daysLeft();
        daysLeft.setText(value);

        value=getString(R.string.finishedView);

        if(task.isFinished()) {
            value+=getString(R.string.yes);
        } else {
            value+=getString(R.string.no);
        }
        finished.setText(value);
        try {
            subtaskAdapter = new SubtaskAdapter((ArrayList<Subtask>) task.getSubtaskList());
            subtaskRV.setAdapter(subtaskAdapter);
            subtaskRV.setLayoutManager(new LinearLayoutManager(this));
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG,e.toString());
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Task itask=new Task();
        for(int i=0; i<taskList.size();i++){
            if(task.isTheSame(taskList.get(i))){
                itask=taskList.get(i);
                break;
            }
        }
        taskList.remove(itask);
        task.setSubtaskList((ArrayList<Subtask>)subtaskAdapter.getList());
        taskList.add(task);
        taskList.sort();
        taskList.saveToFile();
    }

}