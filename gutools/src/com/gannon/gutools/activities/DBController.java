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
	 * 
	 */
	public static final String TABLE_COURSES = "courses";
	public static final String TABLE_ASSIGNMENTS = "assignments";
	public static final String COLUMN_COURSE = "course";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PROFESSOR = "professor";
	public static final String COLUMN_INFO = "info";
	public static final String COLUMN_CREDIT = "credit";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_M = "is_monday";
	public static final String COLUMN_T = "is_tuesday";
	public static final String COLUMN_W = "is_wednesday";
	public static final String COLUMN_TH = "is_thursday";
	public static final String COLUMN_F = "is_friday";
	
	private static final String DATABASE_NAME = "courses.db";
	private static final int DATABASE_VERSION = 5;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
	    + TABLE_COURSES + "(" + COLUMN_ID
	    + " integer primary key autoincrement, " + COLUMN_NAME
	    + " text not null, " + COLUMN_PROFESSOR
	    + " text not null, " + COLUMN_INFO
	    + " text not null, " + COLUMN_CREDIT
	    + " text not null, " + COLUMN_TIME
	    + " text not null, " + COLUMN_M
	    + " integer not null, " + COLUMN_T
	    + " integer not null, " + COLUMN_W
	    + " integer not null, " + COLUMN_TH
	    + " integer not null, " + COLUMN_F
	    + " integer not null);";
	
	private static final String ASSIGNMENT_DB_CREATE = "create table "
			+ TABLE_ASSIGNMENTS +"(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null, " + COLUMN_COURSE
			+ " text not null, " + COLUMN_DATE
			+ " text not null);";
	
	public DBController(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE);
	  database.execSQL(ASSIGNMENT_DB_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  Log.w(DBController.class.getName(),
	      "Upgrading database from version " + oldVersion + " to "
	          + newVersion + ", which will destroy all old data");
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
	  onCreate(db);
	}
		
}
