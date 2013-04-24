package com.gannon.gutools.activities;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
public class CoursesDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private DBController dbHelper;
	  private String[] allColumns = { DBController.COLUMN_ID,
	      DBController.COLUMN_NAME, DBController.COLUMN_INFO, 
	      DBController.COLUMN_PROFESSOR, DBController.COLUMN_CREDIT };

	  public CoursesDataSource(Context context) {
	    dbHelper = new DBController(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Course createCourse(String name, String info, String prof, String cred) {
	    ContentValues values = new ContentValues();
	    values.put(DBController.COLUMN_NAME, name);
	    values.put(DBController.COLUMN_INFO, info);
	    values.put(DBController.COLUMN_PROFESSOR, prof);
	    values.put(DBController.COLUMN_CREDIT, cred);
	  
	    long insertId = database.insert(DBController.TABLE_COURSES, null,
	        values);
	    Cursor cursor = database.query(DBController.TABLE_COURSES,
	        allColumns, DBController.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Course newCourse = cursorToCourse(cursor);
	    cursor.close();
	    return newCourse;
	  }

	  public void deleteCourse(Course course) {
	    long id = course.getId();
	    System.out.println("Comment deleted with id: " + id);
	    database.delete(DBController.TABLE_COURSES, DBController.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<Course> getAllCourses() {
	    List<Course> courses = new ArrayList<Course>();

	    Cursor cursor = database.query(DBController.TABLE_COURSES,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Course course = cursorToCourse(cursor);
	      courses.add(course);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return courses;
	  }

	  private Course cursorToCourse(Cursor cursor) {
	    Course course = new Course();
	    course.setId(cursor.getLong(0));
	    course.setName(cursor.getString(1));
	    course.setInfo(cursor.getString(2));
	    course.setProfessor(cursor.getString(3));
	    course.setCredit(cursor.getString(4));
	    return course;
	  } 
}
