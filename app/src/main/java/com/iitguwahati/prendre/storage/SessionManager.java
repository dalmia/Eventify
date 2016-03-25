package com.iitguwahati.prendre.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "SmartSchool";
	
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	private static final String KEY_IS_CHOICE_MADE = "isChoiceMade";
	private static final String KEY_CHOICE = "choice";
	private static final String KEY_NETWORK_INFO = "network_info";
	private static final String KEY_SCHOOL_NAME = "schoolName";
	private static final String KEY_EVENTS_STARTER = "eventsStarter";
	private static final String KEY_TASKS_STARTER = "tasksStarter";
	private static final String KEY_GET_STARTED_DONE = "getStartedDone";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}

	public void setGetStartedDone(boolean done) {

		editor.putBoolean(KEY_GET_STARTED_DONE, done);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}



	public void setSchool(String school) {

		editor.putString(KEY_SCHOOL_NAME, school);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}


	public void setChoice(boolean isChoiceMade, String choice) {

		editor.putBoolean(KEY_IS_CHOICE_MADE, isChoiceMade);
		editor.putString(KEY_CHOICE, choice);
		// commit changes
		editor.commit();

		Log.d(TAG, "User choice session modified!");
	}

	public void setNetState(boolean netState) {

		editor.putBoolean(KEY_NETWORK_INFO, netState);

		// commit changes
		editor.commit();

		Log.d(TAG, "User choice session modified!");
	}

	public void setEventsStarted(int eventsStarted){
		editor.putInt(KEY_EVENTS_STARTER, eventsStarted);
		editor.commit();
	}

	public void setTasksStarted(int tasksStarted){
		editor.putInt(KEY_TASKS_STARTER, tasksStarted);
		editor.commit();
	}

	
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}

	public boolean isChoiceMade(){
		return pref.getBoolean(KEY_IS_CHOICE_MADE, false);
	}

	public int isEventsStarted(){
		return pref.getInt(KEY_EVENTS_STARTER, 0);

	}

	public int isTasksStarted(){
		return pref.getInt(KEY_TASKS_STARTER, 0);
	}

	public String choice(){
		return pref.getString(KEY_CHOICE, "");
	}

	public String getSchoolName(){
		return pref.getString(KEY_SCHOOL_NAME, null );
	}

	public boolean netState(){
		return pref.getBoolean(KEY_NETWORK_INFO, false);
	}

	public boolean isGetStartedDone(){
		return pref.getBoolean(KEY_GET_STARTED_DONE, false);
	}

}
