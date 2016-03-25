package com.iitguwahati.prendre.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iitguwahati.prendre.R;
import com.iitguwahati.prendre.app.AppController;
import com.iitguwahati.prendre.storage.SQLiteHandler;
import com.iitguwahati.prendre.storage.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button register;
    String usernameValue, passwordValue;
    AutoCompleteTextView username, password;
    ArrayAdapter<String> nameList, webmailList;
    ProgressDialog pDialog;
    SessionManager session;
    SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new SQLiteHandler(getApplicationContext());
        username = (AutoCompleteTextView) findViewById(R.id.username);
        password = (AutoCompleteTextView) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        nameList = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.names));
        webmailList = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.webmails));
        username.setAdapter(nameList);
        password.setAdapter(webmailList);
        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                username.showDropDown();
                return false;
            }
        });


        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                password.showDropDown();
                return false;
            }
        });

        username.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                password.setText(webmailList.getItem(position));
            }
        });
        password.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                username.setText(nameList.getItem(position));
            }
        });

        pDialog = new ProgressDialog(this);
        session = new SessionManager(this);
        if(session.isLoggedIn()){
            Intent intent = new Intent(this, UserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        register.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(session.isLoggedIn()){
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                usernameValue = username.getText().toString();
                passwordValue = password.getText().toString();
                if (!usernameValue.isEmpty() && !passwordValue.isEmpty()) {
                    registerUser();

                } else {
                    Toast.makeText(RegisterActivity.this, "Enter Your Credentials", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    public void registerUser() {
        String tag_string_req = "req_tag_register";
        pDialog.setMessage("Registering New User");
        pDialog.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", passwordValue);
        params.put("password", usernameValue);


        JSONObject json = new JSONObject(params);
        Log.d("Sent", json.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "http://amandalmia.16mb.com/user_place_order/kriti_register.php",
                json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Response", response.toString());
                    String error= response.getString("error");
                    if(error.equals("false")){
                        JSONObject user = new JSONObject(response.getString("user"));
                        JSONObject details = new JSONObject(user.getString("details"));
                        JSONArray timings = new JSONArray(user.getString("timings"));
                        String name = details.getString("name");
                        String branch = details.getString("branch");
                        String roll_number = details.getString("roll_number");
                        String year = details.getString("year");
                        String hostel = details.getString("hostel");
                        for(int i = 0; i<timings.length(); i++){
                            JSONObject dayTimings= timings.getJSONObject(i);
                            String day = dayTimings.getString("day");
                            String timing = dayTimings.getString("timings");
                            String lab = dayTimings.getString("lab");
                            String lab_timings = dayTimings.getString("lab_timings");
                            db.addTiming(day, timing,dayTimings.getString("location"), lab, lab_timings,dayTimings.getString("lab_location") );
                        }
                        db.addUser(name, usernameValue,roll_number,branch, year,hostel );
                        Intent intent = new Intent(RegisterActivity.this, UserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        session.setLogin(true);
                        pDialog.hide();
                        finish();
                    }

                    else{
                        Toast.makeText(RegisterActivity.this, response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                        pDialog.hide();
                    }



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
                Toast.makeText(RegisterActivity.this, "Error Registering", Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

}
