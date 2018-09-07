package com.jhartmayer.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ToDoListFragment extends Fragment {

    List<Task> tasks = new ArrayList<Task>();
    ArrayAdapter<Task> adapter = null;
    private DBHelper dbHelper = null;

    private final static String TAG = "ToDoListActivity";

    public ToDoListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try{
            dbHelper = new DBHelper(getActivity());
            tasks = dbHelper.selectAll();
        } catch (Exception e) {
            Log.d(TAG, "onCreate: DBHelper threw exception e: " + e);
            e.printStackTrace();
        }

        ListView list = getActivity().findViewById(R.id.task_tasks);
        adapter = new TaskAdapter(getActivity(), R.layout.row, tasks);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = adapter.getItem(position);
                String addInfo = task.getAdtnlDescription();
                showAdditionalInfo(addInfo);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onDelete(view, position);
                return true;
            }
        });

        Button saveBtn = getActivity().findViewById(R.id.save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });

    }

    public void onSave() {
        Task task = new Task();

        EditText title = getActivity().findViewById(R.id.task_title);
        EditText description = getActivity().findViewById(R.id.task_desc);
        EditText dueDate = getActivity().findViewById(R.id.task_dueDate);
        EditText addInfo = getActivity().findViewById(R.id.task_adtnlInfo);

        String taskTitle = title.getText().toString();
        String taskDesc = description.getText().toString();
        String taskDueDate = dueDate.getText().toString();
        String taskAddInfo = addInfo.getText().toString();
        if(TextUtils.isEmpty(taskTitle) || TextUtils.isEmpty(taskDesc) ||
                TextUtils.isEmpty(taskDueDate) || TextUtils.isEmpty(taskAddInfo)) {
            showMissingInfoAlert();
        }

        task.setTitle(taskTitle);
        task.setDescription(taskDesc);
        task.setDueDate(taskDueDate);
        task.setAdtnlDescription(taskAddInfo);

        long taskId = 0;
        if (dbHelper != null) {
            taskId = dbHelper.insert(task);
            task.setId(taskId);
        }

        adapter.add(task);
        adapter.notifyDataSetChanged();

        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onDelete(View view, int position) {
        Task task = adapter.getItem(position);

        if(task != null) {
            String item = "deleting: " + task.getTitle().toString();
            Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            Log.d(TAG, " onItemLongClick: " + task.getTitle());

            if(dbHelper != null) {
                dbHelper.deleteRecord(task.getId());
            }

            adapter.remove(task);
            adapter.notifyDataSetChanged();
        }
    }

    public void showAdditionalInfo(String addInfo) {
        Toast.makeText(getActivity(), addInfo, Toast.LENGTH_LONG).show();
    }

    public void showMissingInfoAlert() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
        alertDialogBuilder.setTitle(R.string.alert_title);
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialogBuilder.setMessage(R.string.alert_message)
            .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }
}
