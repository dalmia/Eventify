package com.iitguwahati.prendre.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iitguwahati.prendre.R;
import com.iitguwahati.prendre.storage.SQLiteHandler;
import com.iitguwahati.prendre.storage.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {


    private ChatAdapter mAdapter;
    private ArrayList<HashMap<String, String>> chats;
    RecyclerView mRecyclerView;
    private ArrayList<HashMap<String, String>> mDataset, saved_tasks;
    private org.solovyev.android.views.llm.LinearLayoutManager mLayoutManager;
    HashMap<String, String> chat_params, task_params;
    SQLiteHandler db;
    LinearLayout tasksStarter, tasks;
    SessionManager sessionManager;
    FloatingActionButton addTask;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;
    private Calendar myCalendar;
    EditText dateofSubmission, timeofCompletion;
    int count = 0;
    View addingView;
    ArrayList<HashMap<String, String>> chatList;


    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.chats);
        tasks = (LinearLayout) view.findViewById(R.id.tasks);
        tasksStarter = (LinearLayout) view.findViewById(R.id.task_starter);
        sessionManager = new SessionManager(getActivity());
        saved_tasks = db.getTasks();

        if(sessionManager.isTasksStarted()==1){
            tasksStarter.setVisibility(View.GONE);
        }
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        addingView = inflater.inflate(R.layout.add_tasks, null, false);

        chat_params = new HashMap<>();
        chatList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        addTask  = (FloatingActionButton) view.findViewById(R.id.fab);
        dateofSubmission = (EditText) addingView.findViewById(R.id.dateOfCompletion);
        timeofCompletion = (EditText) addingView.findViewById(R.id.timeOfCompletion);

        timeofCompletion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new TimePickerDialog(getActivity(),
                            time, myCalendar.get(Calendar.HOUR_OF_DAY),
                            myCalendar.get(Calendar.MINUTE), true).show();
                }
            }
        });
        dateofSubmission.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    new DatePickerDialog(getActivity(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                timeofCompletion.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            }
        };
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
                d.setTitle("Add Task");
                if(addingView.getParent()!=null){
                    ((ViewGroup)addingView.getParent()).removeView(addingView);
                }
                d.setView(addingView);
                d.setCancelable(true);
                d.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText label = (EditText) addingView.findViewById(R.id.label);
                        CheckBox remindMe = (CheckBox) addingView.findViewById(R.id.remindMe);
                        String reminder= "false";
                        //addTruck(type.getText().toString());
                        if(!label.getText().toString().isEmpty()&&!dateofSubmission.getText().toString().isEmpty()&&!timeofCompletion.getText().toString().isEmpty()){
                            task_params = new HashMap<String, String>();
                            task_params.put("date", dateofSubmission.getText().toString());
                            task_params.put("time", timeofCompletion.getText().toString());
                            task_params.put("label", label.getText().toString());
                            if(remindMe.isChecked()){
                                task_params.put("remind", "true");
                                reminder = "true";
                            }
                            else {
                                task_params.put("date", "false");
                                reminder = "false";
                            }
                            int size = mAdapter.getItemCount();
                            mAdapter.add(size, task_params);
                            if(sessionManager.isTasksStarted()==0){
                                sessionManager.setTasksStarted(1);
                                tasksStarter.setVisibility(View.GONE);
                            }
                            db.addTask(label.getText().toString(),dateofSubmission.getText().toString(),timeofCompletion.getText().toString(),reminder);
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getActivity(), "Fill out all the details first", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                d.show();
            }
        });
        // use a linear layout manager
        mLayoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mAdapter = new ChatAdapter(chatList, getActivity());
        int original_size = mAdapter.getItemCount();
        mRecyclerView.setAdapter(mAdapter);
        for(int j =0; j<original_size; j++){
            mAdapter.remove(j);
        }

        for(int i=0; i<saved_tasks.size(); i++){
            int size = mAdapter.getItemCount();
            mAdapter.add(size, saved_tasks.get(i));
        }
    }

    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


        Context context;

        /**
         * Custom onClickListener as recycler view does not support onCLickListener by default.
         */

        private final View.OnClickListener mOnClickListener = new TasksFragment.RecyclerViewOnClickListener();

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder


        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView date, time, label;
            CheckBox reminded;

            public ViewHolder(View v) {
                super(v);

                date = (TextView) v.findViewById(R.id.date);
                time = (TextView) v.findViewById(R.id.time);
                label = (TextView) v.findViewById(R.id.label);
                reminded = (CheckBox) v.findViewById(R.id.reminded);

            }
        }

        public void add(int position, HashMap<String, String> item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {

            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ChatAdapter(ArrayList<HashMap<String, String>> myDataset, Context context) {
            mDataset = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            v.setOnClickListener(mOnClickListener);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final HashMap<String, String> person = mDataset.get(position);
            holder.date.setText(person.get("date"));
            holder.time.setText(person.get("time"));
            holder.label.setText(person.get("label"));
            if(person.get("remind").equals("true")){
                holder.reminded.setChecked(true);
            }

            holder.reminded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        Toast.makeText(getActivity(), "Task set to Reminder", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Task removed from Reminder", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


    }

    private void updateLabel() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateofSubmission.setText(sdf.format(myCalendar.getTime()));
    }


    public class RecyclerViewOnClickListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            TextView date, time, recycler_label;
            CheckBox reminded;
            final int position = mRecyclerView.getChildAdapterPosition(v);
            AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
            d.setTitle("Add Task");
            if(addingView.getParent()!=null){
                ((ViewGroup)addingView.getParent()).removeView(addingView);
            }
            EditText label = (EditText) addingView.findViewById(R.id.label);
            CheckBox remindMe = (CheckBox) addingView.findViewById(R.id.remindMe);
            date = (TextView) v.findViewById(R.id.date);
            time = (TextView) v.findViewById(R.id.time);

            recycler_label = (TextView) v.findViewById(R.id.label);
            reminded = (CheckBox) v.findViewById(R.id.reminded);
            label.setText(recycler_label.getText().toString());
            dateofSubmission.setText(date.getText().toString());
            timeofCompletion.setText(time.getText().toString());
            if(!reminded.isChecked())
                remindMe.setChecked(false);
            d.setView(addingView);
            d.setCancelable(true);
            d.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String reminder="false";
                    EditText label = (EditText) addingView.findViewById(R.id.label);
                    CheckBox remindMe = (CheckBox) addingView.findViewById(R.id.remindMe);
                    //addTruck(type.getText().toString());
                    if(!label.getText().toString().isEmpty()&&!dateofSubmission.getText().toString().isEmpty()&&!timeofCompletion.getText().toString().isEmpty()){
                        task_params = new HashMap<String, String>();
                        task_params.put("date", dateofSubmission.getText().toString());
                        task_params.put("time", timeofCompletion.getText().toString());
                        task_params.put("label", label.getText().toString());
                        if(remindMe.isChecked()){
                            task_params.put("remind", "true");
                            reminder = "true";
                        }

                        else {
                            task_params.put("date", "false");
                            reminder = "false";
                        }
                        mAdapter.remove(position);
                        mAdapter.add(position, task_params);
                        db.updateTask(label.getText().toString(),dateofSubmission.getText().toString(),timeofCompletion.getText().toString(),reminder);
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(getActivity(), "Fill out all the details first", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            d.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            d.show();
        }
    }




}
