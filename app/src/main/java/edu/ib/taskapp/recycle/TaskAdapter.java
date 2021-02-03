package edu.ib.taskapp.recycle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.ib.taskapp.AddTaskActivity;
import edu.ib.taskapp.R;
import edu.ib.taskapp.TaskActivity;
import edu.ib.taskapp.task.Task;
import edu.ib.taskapp.TasksList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private TasksList taskList;

    public TaskAdapter(TasksList taskList){
        this.taskList=taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.recycle_task, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task=taskList.get(position);
        TextView textView=holder.text;
        CheckBox checkBox=holder.done;
        switch(task.getPriority()){
            case Low:
                textView.setTextColor(Color.BLUE);
                break;
            case Normal:
                textView.setTextColor(Color.GRAY);
                break;
            case High:
                textView.setTextColor(Color.RED);
                break;
        }
        if(task.isAfterDate()){
            textView.setTextColor(Color.rgb(184,3,255));
        }

        textView.setText(task.getName());
        if(task.isFinished()){
            checkBox.setChecked(true);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        ImageButton imageButton=holder.deleteButton;


        checkBox.setOnClickListener((View.OnClickListener) view->{
            boolean value=checkBox.isChecked();
            task.setFinished(value);
            if (value){
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textView.setPaintFlags(textView.getPaintFlags() &~ Paint.STRIKE_THRU_TEXT_FLAG);
            }
            task.setFinished(value);
        });
        imageButton.setOnClickListener((View.OnClickListener) view ->{
            int index=taskList.indexOf(task);
            taskList.remove(index);
            this.notifyItemRemoved(index);
        });
        textView.setOnClickListener((View.OnClickListener)view->{
            Context context=view.getContext();
            Intent intent=new Intent(context,TaskActivity.class);
            intent.putExtra(TaskActivity.EXTRA_TASK,task);
            context.startActivity(intent);
        });
        textView.setOnLongClickListener((View.OnLongClickListener) view->{
            Context context=view.getContext();
            Intent intent=new Intent(context, AddTaskActivity.class);
            intent.putExtra(AddTaskActivity.EXTRA_MODE,AddTaskActivity.Mode.Edit);
            intent.putExtra(AddTaskActivity.EXTRA_TASK,task);
            context.startActivity(intent);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox done;
        TextView text;
        ImageButton deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            done=(CheckBox) itemView.findViewById(R.id.cb_done_task);
            text=(TextView) itemView.findViewById(R.id.taskView);
            deleteButton= (ImageButton) itemView.findViewById(R.id.ibtn_delete);
        }
    }
}
