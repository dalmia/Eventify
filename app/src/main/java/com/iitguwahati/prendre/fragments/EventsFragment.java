package com.iitguwahati.prendre.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.iitguwahati.prendre.R;
import com.iitguwahati.prendre.helper.Events;
import com.iitguwahati.prendre.storage.SQLiteHandler;
import com.iitguwahati.prendre.storage.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {


    LinearLayout eventsStarted, events;
    private ChatAdapter mAdapter;
    ProgressDialog progressDialog;
    Firebase myFirebaseRef;
    ProgressDialog pDialog;
    private ArrayList<HashMap<String, String>> chats, user;
    RecyclerView mRecyclerView;
    private ArrayList<HashMap<String, String>> mDataset, eventsList;
    private org.solovyev.android.views.llm.LinearLayoutManager mLayoutManager;
    HashMap<String, String> chat_params, event_params;
    SQLiteHandler db;
    SessionManager sessionManager;
    FloatingActionButton addTask;
    int count = 0;
    ArrayList<HashMap<String, String>> chatList;


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sessionManager = new SessionManager(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        events = (LinearLayout) view.findViewById(R.id.events);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting Your Events");
        progressDialog.show();
        user = db.getUserDetails();
        String name = user.get(0).get("name").toLowerCase();
        myFirebaseRef = new Firebase("https://eventifycodeio.firebaseio.com/");
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        // getEvents();
        eventsStarted = (LinearLayout) view.findViewById(R.id.events_starter);
        if (sessionManager.isEventsStarted() == 1) {
            eventsStarted.setVisibility(View.GONE);
        }


        eventsList = db.getEvents();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.events_list);
        chat_params = new HashMap<>();
        chatList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mAdapter = new ChatAdapter(chatList, getActivity());
        if (sessionManager.isEventsStarted() == 0) {
            sessionManager.setEventsStarted(1);
            eventsStarted.setVisibility(View.GONE);
        }
        int original_size = mAdapter.getItemCount();
        mRecyclerView.setAdapter(mAdapter);
        for (int j = 0; j < original_size; j++) {
            mAdapter.remove(j);
        }


        for (int i = 0; i < eventsList.size(); i++) {
            int size = mAdapter.getItemCount();
            mAdapter.add(size, eventsList.get(i));
        }


        myFirebaseRef.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                progressDialog.hide();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Events post = postSnapshot.getValue(Events.class);
                    System.out.println(post.getSubject() + " - " + post.getDate());
                    event_params = new HashMap<>();
                    event_params.put("subject", post.getSubject());
                    event_params.put("date", post.getDate());
                    event_params.put("time", post.getTime());
                    event_params.put("venue", post.getVenue());
                    Log.d("Params" ,event_params.toString());
                    int size = mAdapter.getItemCount();
                    mAdapter.add(size, event_params);
                    db.addEvent(post.getSubject(), post.getDate(), post.getTime(), post.getVenue());
                    mLayoutManager.scrollToPosition(mAdapter.getItemCount());
                }
                Toast.makeText(getActivity(), "Event Added!!! Scroll Up To View", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.events_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);

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
            public TextView subject, date, time, venue;


            public ViewHolder(View v) {
                super(v);

                subject = (TextView) v.findViewById(R.id.subject);
                date = (TextView) v.findViewById(R.id.date);
                time = (TextView) v.findViewById(R.id.time);
                venue = (TextView) v.findViewById(R.id.venue);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_card_view, parent, false);
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
            holder.subject.setText(person.get("subject"));
            holder.date.setText(person.get("date"));
            holder.time.setText(person.get("time"));
            holder.venue.setText(person.get("venue"));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


    }

  /*  public void getEvents() {
        String tag_string_req = "req_tag_register";
        pDialog.setMessage("Getting Your Events");
        pDialog.show();
        final HashMap<String, String> params = new HashMap<String, String>();
        JSONObject json = new JSONObject(params);
        Log.d("Sent", json.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "http://amandalmia.16mb.com/dummy_kriti.php",
                json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Response", response.toString());
                    String subject = response.getString("subject");
                    String date = response.getString("date");
                    String time = response.getString("time");
                    String venue = response.getString("venue");
                    event_params = new HashMap<>();
                    event_params.put("subject", subject);
                    event_params.put("date", date);
                    event_params.put("time", time);
                    event_params.put("venue", venue);
                    for(int i =0;i<2; i++) {
                        int size = mAdapter.getItemCount();
                        mAdapter.add(size, event_params);
                        db.addEvent(date, time, subject, venue);
                    }


                    pDialog.hide();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e("Error: ", error.getMessage());
                error.printStackTrace();
                pDialog.hide();
                Toast.makeText(getActivity(), "Error Getting Events", Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }*/


}
