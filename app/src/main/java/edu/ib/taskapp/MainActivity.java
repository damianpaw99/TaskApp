package edu.ib.taskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.io.File;
import java.time.LocalDate;

import edu.ib.taskapp.recycle.TaskAdapter;
import edu.ib.taskapp.task.Task;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG="EDUIB";
    public static final String FOLDER="data";
    public static final String SETTINGS_FILE="settings.txt";
    public static final String TASK_FILE="taskList.xml";

    private TasksList tasksList;
    private TasksList today=new TasksList();
    private TasksList tomorrow =new TasksList();
    private TasksList week=new TasksList();
    private TasksList future=new TasksList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        File file =new File(getExternalFilesDir(FOLDER),TASK_FILE);
        tasksList=new TasksList(file);
        tasksList.loadFromFile();

        for(int i=0;i<tasksList.size();i++){
            Task task=tasksList.get(i);
            LocalDate date=task.getDate();
            if(date.isEqual(LocalDate.now()) || date.isBefore(LocalDate.now())){
                today.add(task);
            } else if(date.isEqual(LocalDate.now().plusDays(1))){
                tomorrow.add(task);
            } else if(date.isBefore(LocalDate.now().plusDays(7))){
                week.add(task);
            } else future.add(task);
        }
        TaskAdapter todayAdapter=new TaskAdapter(today);
        TaskAdapter tomorrowAdapter=new TaskAdapter(tomorrow);
        TaskAdapter weekAdapter=new TaskAdapter(week);
        TaskAdapter futureAdapter=new TaskAdapter(future);

        RecyclerView todayRV = (RecyclerView) findViewById(R.id.rvToday);

        RecyclerView tomorrowRV = (RecyclerView) findViewById(R.id.rvTomorrow);

        RecyclerView weekRV = (RecyclerView) findViewById(R.id.rvWeek);

        RecyclerView futureRV = (RecyclerView) findViewById(R.id.rvFuture);

        todayRV.setAdapter(todayAdapter);
        todayRV.setLayoutManager(new LinearLayoutManager(this));

        tomorrowRV.setAdapter(tomorrowAdapter);
        tomorrowRV.setLayoutManager(new LinearLayoutManager(this));

        weekRV.setAdapter(weekAdapter);
        weekRV.setLayoutManager(new LinearLayoutManager(this));

        futureRV.setAdapter(futureAdapter);
        futureRV.setLayoutManager(new LinearLayoutManager(this));

    }


    public void addTask(View view) {
        Intent intent=new Intent(this,AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.EXTRA_MODE,AddTaskActivity.Mode.Add);
        updateList();
        intent.putExtra(AddTaskActivity.EXTRA_TASK_LIST,tasksList);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveDisplayed();
    }

    private void updateList(){
        tasksList.clear();
        tasksList.addAll(today);
        tasksList.addAll(tomorrow);
        tasksList.addAll(week);
        tasksList.addAll(future);
        tasksList.sort();
    }

    private void saveDisplayed(){
        updateList();
        tasksList.saveToFile();
    }
}