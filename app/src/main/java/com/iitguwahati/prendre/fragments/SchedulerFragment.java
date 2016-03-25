package com.iitguwahati.prendre.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iitguwahati.prendre.R;
import com.iitguwahati.prendre.storage.SQLiteHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchedulerFragment extends Fragment {


    private ChatAdapter mAdapter;
    private ClassAdapter classAdapter;
    private ArrayList<HashMap<String, String>> timings;
    RecyclerView  mRecyclerView, classRecyclerView;
    private ArrayList<HashMap<String, String>> mDataset, mClassDataset;
    private org.solovyev.android.views.llm.LinearLayoutManager mLayoutManager, classLayoutManager;
    HashMap<String, String> timing_params;
    SQLiteHandler db;
    int count = 0;
    ArrayList<HashMap<String, String>> chatList, classList;

    public SchedulerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scheduler, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new SQLiteHandler(getActivity().getApplicationContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lab_timings);
        classRecyclerView = (RecyclerView) view.findViewById(R.id.class_timings);
        timing_params = new HashMap<>();
        chatList = new ArrayList<>();
        classList = new ArrayList<>();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        classRecyclerView.setHasFixedSize(false);
        timings = db.getTimingDetails();

        // use a linear layout manager
        mLayoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        classLayoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        classRecyclerView.setLayoutManager(classLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        classRecyclerView.setHasFixedSize(false);
        mAdapter = new ChatAdapter(chatList, getActivity());
        classAdapter = new ClassAdapter(classList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        classRecyclerView.setAdapter(classAdapter);


        for(int i= 0; i<timings.size(); i++){
            HashMap<String, String> params = timings.get(i);
            if(params.get("lab").equals("y")) {
                timing_params = new HashMap<>();
                timing_params.put("time", params.get("lab_timing"));
                timing_params.put("day", params.get("day"));
                timing_params.put("location", params.get("lab_location"));
                mAdapter.add(count, timing_params);
                count ++;
            }

            count = 0;
            timing_params = new HashMap<>();
            timing_params.put("time", params.get("timing"));
            timing_params.put("day", params.get("day"));
            timing_params.put("location", params.get("location"));
            classAdapter.add(i, timing_params);

        }
        /*for (int i = 0; i < 3; i++) {
            params = new HashMap<>();
            params.put("time", "2-5 pm");
            params.put("day", "Monday");
            params.put("location", "Core 4");
            mAdapter.add(i, params);
        }

        for (int i = 0; i < 3; i++) {
            params = new HashMap<>();
            params.put("time", "8-12 pm");
            params.put("day", "Monday");
            params.put("location", "Lecture Hall 1");
            classAdapter.add(i, params);
        }*/


    }

    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


        Context context;

        /**
         * Custom onClickListener as recycler view does not support onCLickListener by default.
         */


        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder


        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView day, time, location;


            public ViewHolder(View v) {
                super(v);

                day = (TextView) v.findViewById(R.id.day);
                time = (TextView) v.findViewById(R.id.timing);
                location = (TextView) v.findViewById(R.id.location);

            }
        }

        public void add(int position, HashMap<String, String> item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(HashMap<String, String> item) {
            int position = mDataset.indexOf(item);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final HashMap<String, String> person = mDataset.get(position);
            holder.day.setText(person.get("day"));
            holder.time.setText(person.get("time"));
            holder.location.setText(person.get("location"));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


    }


    public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {


        Context context;

        /**
         * Custom onClickListener as recycler view does not support onCLickListener by default.
         */


        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder


        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView day, time, location;


            public ViewHolder(View v) {
                super(v);

                day = (TextView) v.findViewById(R.id.day);
                time = (TextView) v.findViewById(R.id.timing);
                location = (TextView) v.findViewById(R.id.location);

            }
        }

        public void add(int position, HashMap<String, String> item) {
            mClassDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(HashMap<String, String> item) {
            int position = mClassDataset.indexOf(item);
            mClassDataset.remove(position);
            notifyItemRemoved(position);
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ClassAdapter(ArrayList<HashMap<String, String>> myDataset, Context context) {
            mClassDataset = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final HashMap<String, String> person = mClassDataset.get(position);
            holder.day.setText(person.get("day"));
            holder.time.setText(person.get("time"));
            holder.location.setText(person.get("location"));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mClassDataset.size();
        }


    }


}
