/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.iitguwahati.prendre.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "schoolmate";

	// Login table name
	private static final String TABLE_STUDENT = "user";
	private static final String TABLE_TIMINGS = "timings";
	private static final String TABLE_TASKS = "tasks";
	private static final String TABLE_EVENTS = "events";

	// Login Table Columns names
	private static final String KEY_DAY = "day";
	private static final String KEY_NAME = "name";
	private static final String KEY_USER = "username";
	private static final String KEY_YEAR= "year_student";
	private static final String KEY_BRANCH= "branch_student";
	private static final String KEY_ROLL= "roll_student";
	private static final String KEY_HOSTEL= "hostel";
	private static final String KEY_TIMINGS= "timings";
	private static final String KEY_LOCATION= "location";
	private static final String KEY_LAB_TIMINGS= "lab_timings";
	private static final String KEY_LAB= "lab";
	private static final String KEY_LAB_LOCATION= "lab_location";
	private static final String KEY_LABEL= "label";
	private static final String KEY_DATE_OF_COMPLETION= "dateofCompletion";
	private static final String KEY_TIME_OF_COMPLETION= "timeofCompletion";
	private static final String KEY_REMIND= "remind";
	private static final String KEY_DATE= "date";
	private static final String KEY_TIME= "time";
	private static final String KEY_SUBJECT= "subject";
	private static final String KEY_VENUE= "venue";



	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENT + "("
				+ KEY_NAME + " TEXT,"
				+ KEY_USER + " TEXT,"
				+ KEY_ROLL + " TEXT,"
				+ KEY_BRANCH + " TEXT,"
				+ KEY_YEAR + " TEXT,"
				+ KEY_HOSTEL + " TEXT"
				+ ")";


		String CREATE_STUDENT_TIMINGS_TABLE = "CREATE TABLE " + TABLE_TIMINGS + "("
				+ KEY_DAY + " TEXT,"
				+ KEY_TIMINGS + " TEXT,"
				+ KEY_LOCATION + " TEXT,"
				+ KEY_LAB + " TEXT,"
				+ KEY_LAB_TIMINGS + " TEXT,"
				+ KEY_LAB_LOCATION + " TEXT"
				+ ")";

		String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
				+ KEY_LABEL + " TEXT,"
				+ KEY_DATE_OF_COMPLETION + " TEXT,"
				+ KEY_TIME_OF_COMPLETION+ " TEXT,"
				+ KEY_REMIND+ " TEXT"
				+ ")";

		String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
				+ KEY_DATE + " TEXT,"
				+ KEY_TIME + " TEXT,"
				+ KEY_SUBJECT+ " TEXT,"
				+ KEY_VENUE+ " TEXT"
				+ ")";

		db.execSQL(CREATE_STUDENT_TABLE);
		db.execSQL(CREATE_STUDENT_TIMINGS_TABLE);
		db.execSQL(CREATE_TASKS_TABLE);
		db.execSQL(CREATE_EVENTS_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMINGS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String username, String roll, String branch,String year, String hostel) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_USER, username);
		values.put(KEY_ROLL, roll);
		values.put(KEY_BRANCH, branch);
		values.put(KEY_YEAR, year);
		values.put(KEY_HOSTEL, hostel);


		// Inserting Row
		long id = db.insert(TABLE_STUDENT, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	public void addEvent(String date, String time, String subject, String venue) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, date);
		values.put(KEY_TIME, time);
		values.put(KEY_SUBJECT, subject);
		values.put(KEY_VENUE, venue);

		// Inserting Row
		long id = db.insert(TABLE_EVENTS, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	public void addTask(String label, String date, String time,String remind) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LABEL, label);
		values.put(KEY_DATE_OF_COMPLETION, date);
		values.put(KEY_TIME_OF_COMPLETION, time);
		values.put(KEY_REMIND, remind);
		// Inserting Row
		long id = db.insert(TABLE_TASKS, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}


	public void updateTask(String label, String date, String time,String remind) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LABEL, label);
		values.put(KEY_DATE_OF_COMPLETION, date);
		values.put(KEY_TIME_OF_COMPLETION, time);
		values.put(KEY_REMIND, remind);
		// Inserting Row

		String[] value = new String[100];
		value[0] = label;

		long id = db.update(TABLE_TASKS, values,"label=?",value);
		db.close(); // Closing database connection

		Log.d(TAG, "user updated in sqlite: " + id);
	}


	public void addTiming(String day, String timing,String location, String lab, String lab_timing, String lab_location) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DAY, day);
		values.put(KEY_TIMINGS, timing);
		values.put(KEY_LOCATION, location);
		values.put(KEY_LAB, lab);
		values.put(KEY_LAB_TIMINGS, lab_timing);
		values.put(KEY_LAB_LOCATION, lab_location);



		// Inserting Row
		long id = db.insert(TABLE_TIMINGS, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public ArrayList<HashMap<String, String>> getUserDetails() {
		ArrayList<HashMap<String, String>> users = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_STUDENT;
		int i =0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Move to first row
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			HashMap<String, String> paramsInter = new HashMap<>();
			paramsInter.put("name", cursor.getString(0));
			paramsInter.put("username", cursor.getString(1));
			paramsInter.put("roll_number", cursor.getString(2));
			paramsInter.put("branch", cursor.getString(3));
			paramsInter.put("year", cursor.getString(4));
			paramsInter.put("hostel", cursor.getString(5));
			users.add(i,paramsInter);
			cursor.moveToNext();
			i++;


		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + users.toString());

		return users;
	}

	public ArrayList<HashMap<String, String>> getEvents() {
		ArrayList<HashMap<String, String>> users = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;
		int i =0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Move to first row
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			HashMap<String, String> paramsInter = new HashMap<>();
			paramsInter.put("date", cursor.getString(0));
			paramsInter.put("time", cursor.getString(1));
			paramsInter.put("subject", cursor.getString(2));
			paramsInter.put("venue", cursor.getString(3));
			users.add(i,paramsInter);
			cursor.moveToNext();
			i++;


		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + users.toString());

		return users;
	}


	public ArrayList<HashMap<String, String>> getTasks() {
		ArrayList<HashMap<String, String>> users = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
		int i =0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Move to first row
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			HashMap<String, String> paramsInter = new HashMap<>();
			paramsInter.put("label", cursor.getString(0));
			paramsInter.put("date", cursor.getString(1));
			paramsInter.put("time", cursor.getString(2));
			paramsInter.put("remind", cursor.getString(3));
			users.add(i,paramsInter);
			cursor.moveToNext();
			i++;


		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + users.toString());

		return users;
	}


	public ArrayList<HashMap<String, String>>  getTimingDetails() {
		ArrayList<HashMap<String, String>> user = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_TIMINGS;
		int i =0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Move to first row
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			HashMap<String, String> paramsInter = new HashMap<>();
			paramsInter.put("day", cursor.getString(0));
			paramsInter.put("timing", cursor.getString(1));
			paramsInter.put("location", cursor.getString(2));
			paramsInter.put("lab", cursor.getString(3));
			paramsInter.put("lab_timing", cursor.getString(4));
			paramsInter.put("lab_location", cursor.getString(5));
			user.add(i,paramsInter);
			cursor.moveToNext();
			i++;
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_STUDENT, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

	public void deleteCycleSearch() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_STUDENT, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
