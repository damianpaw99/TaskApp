package edu.ib.taskapp.recycle;

import android.content.Context;
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

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.ib.taskapp.R;
import edu.ib.taskapp.task.Subtask;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.ViewHolder> {
    private List<Subtask> list;

    public SubtaskAdapter(ArrayList<Subtask> list){
        this.list=list;
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
        Subtask subtask=list.get(position);
        TextView textView=holder.text;
        CheckBox checkBox=holder.done;

        if(subtask.isAfterDate()){
            textView.setTextColor(Color.rgb(255,0,60));
        }

        textView.setText(subtask.getName());
        if(subtask.isFinished()){
            checkBox.setChecked(true);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        checkBox.setOnClickListener((View.OnClickListener) view->{
            boolean value=checkBox.isChecked();
            subtask.setFinished(value);
            if (value){
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textView.setPaintFlags(textView.getPaintFlags() &~ Paint.STRIKE_THRU_TEXT_FLAG);
            }
            subtask.setFinished(value);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox done;
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            done=(CheckBox) itemView.findViewById(R.id.cb_done_subtask);
            text=(TextView) itemView.findViewById(R.id.subtaskView);
        }
    }
}
