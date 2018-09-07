package com.jhartmayer.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private int layoutResourceId;
    private LayoutInflater inflater;
    private List<Task> tasks;
    public final static String TAG = "TaskAdapter";

    public TaskAdapter(Context context, int layoutResourceId, List<Task> tasks) {
        super(context, layoutResourceId, tasks);
        this.layoutResourceId = layoutResourceId;
        this.tasks = tasks;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskHolder holder = null;

        if (null == convertView) {
            Log.d(TAG, "TaskAdapter getView: rowView null: position " + position);
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new TaskHolder();
            holder.txtTitle = convertView.findViewById(R.id.title);
            holder.txtDescription = convertView.findViewById(R.id.short_desc);
            holder.txtDate = convertView.findViewById(R.id.dueDate);

            convertView.setTag(holder);
        } else {
            Log.d(TAG, "TaskAdapter getView: rowView !null: position " + position);
            holder = (TaskHolder) convertView.getTag();
        }

        Log.d(TAG, "getView tasks: " + tasks.size());

        try{
            Task task = tasks.get(position);
            holder.txtTitle.setText(task.getTitle());
            holder.txtDescription.setText(task.getDescription());
            holder.txtDate.setText(task.getDueDate());
        } catch (Exception e) {
            Log.d(TAG, "getView tasks: " + e + " position was: " + position + " tasks.size: " + tasks.size());
        }

        return convertView;
    }

    static class TaskHolder {
        TextView txtTitle;
        TextView txtDescription;
        TextView txtDate;
    }
}
