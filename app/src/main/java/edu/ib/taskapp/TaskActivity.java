package edu.ib.taskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.ib.taskapp.recycle.SubtaskAdapter;
import edu.ib.taskapp.task.Subtask;
import edu.ib.taskapp.task.Task;

public class TaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK="taskView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_task);

        Intent intent=getIntent();
        Task task=(Task) intent.getSerializableExtra(EXTRA_TASK);

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
        SubtaskAdapter subtaskAdapter = new SubtaskAdapter((ArrayList<Subtask>) task.getSubtaskList());
        subtaskRV.setAdapter(subtaskAdapter);
        subtaskRV.setLayoutManager(new LinearLayoutManager(this));
    }

}