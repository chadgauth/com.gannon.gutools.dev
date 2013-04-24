package com.gannon.gutools.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController extends SQLiteOpenHelper {
	/*
	 * Constructor: Create database named 'Schedule.db'
	 */
	public static final String TABLE_COURSES = "courses";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PROFESSOR = "professor";
	public static final String COLUMN_INFO = "info";
	public static final String COLUMN_CREDIT = "credit";
	
	private static final String DATABASE_NAME = "courses.db";
	private static final int DATABASE_VERSION = 1;
		  // Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
	    + TABLE_COURSES + "(" + COLUMN_ID
	    + " integer primary key autoincrement, " + COLUMN_NAME
	    + " text not null, " + COLUMN_PROFESSOR
	    + " text not null, " + COLUMN_INFO
	    + " text not null, " + COLUMN_CREDIT
	    + " text not null);";
	
	public DBController(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  Log.w(DBController.class.getName(),
	      "Upgrading database from version " + oldVersion + " to "
	          + newVersion + ", which will destroy all old data");
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
	  onCreate(db);
	}
	
	
	/*
	 * Insert functions
	 * Adds extracted data from DataPullActivity into the courses table
	 */
	public void insertCourseProf(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("courseProf", queryValues.get("courseProf"));
		database.insert("courses", null, values);
		database.close();
	}
	public void insertCourseName(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("courseName", queryValues.get("courseName"));
		database.insert("courses", null, values);
		database.close();
	}
	public void insertCourseInfo(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("courseInfo", queryValues.get("courseInfo"));
		database.insert("courses", null, values);
		database.close();
	}
	public void insertCourseCred(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("courseCred", queryValues.get("courseCred"));
		database.insert("courses", null, values);
		database.close();
	}
	/*
	public int updateCourseName(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();	 
	    ContentValues values = new ContentValues();
	    values.put("courseName", queryValues.get("courseName"));
	    return database.update("courses", values, "courseId" + " = ?", new String[] { queryValues.get("courseId") });
	}
	
	public void deleteCourseName(String id) {
		SQLiteDatabase database = this.getWritableDatabase();	 
		String deleteQuery = "DELETE FROM  courses where courseId='"+ id +"'";	
		database.execSQL(deleteQuery);
	}*/
	
	public ArrayList<HashMap<String, String>> getAllCourses() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM courses";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("courseId", cursor.getString(0));
	        	map.put("courseName", cursor.getString(1));
                wordList.add(map);
	        } while (cursor.moveToNext());
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("courseId", cursor.getString(0));
	        	map.put("courseProf", cursor.getString(1));
                wordList.add(map);
	        } while (cursor.moveToNext());
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("courseId", cursor.getString(0));
	        	map.put("courseInfo", cursor.getString(1));
                wordList.add(map);
	        } while (cursor.moveToNext());
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("courseId", cursor.getString(0));
	        	map.put("courseCred", cursor.getString(1));
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return wordList;
	}
	
	public HashMap<String, String> getCourseInfo(String id) {
		HashMap<String, String> wordList = new HashMap<String, String>();
		SQLiteDatabase database = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM courses where courseId='"+id+"'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
	        do {
					//HashMap<String, String> map = new HashMap<String, String>();
	        	wordList.put("courseName", cursor.getString(1));
				   //wordList.add(map);
	        } while (cursor.moveToNext());
	    }				    
	return wordList;
	}
	
	
}
