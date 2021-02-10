package edu.ib.taskapp;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import edu.ib.taskapp.task.Subtask;
import edu.ib.taskapp.task.Task;

/**
 * Activity used to create new task or edit existing task
 */
public class AddTaskActivity extends AppCompatActivity {
    /**
     * Constant String value used to move data about Task between Activities
     */
    public static final String EXTRA_TASK="task";
    /**
     * Constant String value used to move data about TaskList between Activities
     */
    public static final String EXTRA_TASK_LIST="taskList";
    /**
     *Constant String value used to move data about Mode between Activities
     */
    public static final String EXTRA_MODE="mode";
    /**
     * Task to create or edit
     */
    private Task task;
    /**
     * List of all tasks
     */
    private TasksList tasksList;
    /**
     * Mode class object informing if we edit task or create new one
     */
    private Mode mode;

    /**
     * Enum class with two states 'Add' and 'Edit'
     */
    public enum Mode implements Serializable {
        Add,
        Edit
    }

    /**
     * Method loading task if mode is in 'Edit' mode or creating new one when mode is in 'Add' mode
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_task);

        Intent intent=getIntent();
        mode=(Mode) intent.getSerializableExtra(EXTRA_MODE);
        if(mode.equals(Mode.Add)){
            tasksList=(TasksList) intent.getSerializableExtra(EXTRA_TASK_LIST);
            task=new Task();
        }else{
            tasksList=new TasksList(new File(getExternalFilesDir(MainActivity.FOLDER),MainActivity.TASK_FILE));
            tasksList.loadFromFile();
            Task itask=(Task) intent.getSerializableExtra(EXTRA_TASK);
            for(int i=0;i<tasksList.size();i++){
                if(itask.compareTo(tasksList.get(i))==0){
                    task=tasksList.get(i);
                    break;
                }
            }
        }
        Spinner spinner = (Spinner) findViewById(R.id.spPriority);
        EditText name = (EditText) findViewById(R.id.etxtName);
        TextView date = (TextView) findViewById(R.id.txtDatePick);
        CheckBox notification = (CheckBox) findViewById(R.id.cbNotification);
        if(mode.equals(Mode.Add)) {
            spinner.setSelection(1, false);
        } else {
            name.setText(task.getName());
            date.setText(task.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            notification.setChecked(task.isNotification());
        }
    }

    /**
     * Method saving new task or edited task to file
     * @param view
     */
    public void save(View view) {
        EditText name=(EditText) findViewById(R.id.etxtName);
        TextView date=(TextView) findViewById(R.id.txtDatePick);

        CheckBox checkBox=(CheckBox) findViewById(R.id.cbNotification);
        Spinner spinner=(Spinner) findViewById(R.id.spPriority);

        try{
            String taskName=name.getText().toString();
            if(taskName.isEmpty()) throw new TaskException("Task name can't be null");
            String dateString=date.getText().toString();
            if(dateString.isEmpty()) throw new TaskException("Task date can't be null");
            LocalDate taskDate=LocalDate.parse(date.getText(),DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            task.setName(name.getText().toString());
            task.setDate(taskDate);
            task.setNotification(checkBox.isSelected());
            task.setPriority(Task.Priority.valueOf(spinner.getSelectedItem().toString()));
            if(mode.equals(Mode.Add)) {
                tasksList.add(task);
                tasksList.sort();
            } else {
                int index=tasksList.indexOf(task);
                tasksList.remove(index);
                tasksList.add(index,task);
            }

            tasksList.saveToFile();
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        } catch(TaskException e){
            Toast.makeText(this,getString(R.string.empty_field),Toast.LENGTH_SHORT).show();
            Log.e(MainActivity.LOG_TAG,e.toString());
        }
    }

    /**
     * Method canceling adding/editing task
     * @param view
     */
    public void cancel(View view) {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Method create new window with option to chose date by user
     * @param view
     */
    public void pickDate(View view) {
        TextView datePick=(TextView) view;
        DatePickerDialog.OnDateSetListener onDateSetListener= (view1, year, month, dayOfMonth) -> {
            LocalDate date=LocalDate.of(year,month+1,dayOfMonth);
            datePick.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        };

        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(this,android.R.style.Widget_Holo,onDateSetListener,year,month,day);
        Log.d("TEST",year+" "+month+" "+day);
        dialog.show();
    }

    /**
     * Method adding subtask to task (not implemented)
     * @param view
     */
    public void addSubtask(View view) {
        TextView name=(TextView) findViewById(R.id.etxtSubtaskName);
        TextView date=(TextView) findViewById(R.id.etxtSubtaskDate);
        if (!name.getText().toString().isEmpty() && !date.getText().toString().isEmpty() ){
            Subtask subtask=new Subtask();
            subtask.setName(name.getText().toString());
            subtask.setDate(LocalDate.parse(date.getText().toString(),DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            task.addSubtask(subtask);
            name.setText("");
            date.setText("");
        }
    }

}