package com.gannon.gutools.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController extends SQLiteOpenHelper {
	/*
	 * Constructor: Create database named 'Schedule.db'
	 */
	public DBController(Context applicationcontext) {
        super(applicationcontext, "Schedule.db", null, 1);
    }
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE courses ( courseId INTEGER PRIMARY KEY, courseProf TEXT, courseName TEXT, courseInfo TEXT, courseCred TEXT )";
        database.execSQL(query);
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS courses";
		database.execSQL(query);
        onCreate(database);
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
