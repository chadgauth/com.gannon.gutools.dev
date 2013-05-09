package com.gannon.gutools.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AssignmentsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DBController dbHelper;
  private String[] allColumns = { DBController.COLUMN_ID,
      DBController.COLUMN_NAME, DBController.COLUMN_DATE };

  public AssignmentsDataSource(Context context) {
    dbHelper = new DBController(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Assignment createAssignment(String name) {
    ContentValues values = new ContentValues();
    values.put(DBController.COLUMN_NAME, name);
    values.put(DBController.COLUMN_COURSE, " ");
    values.put(DBController.COLUMN_DATE, " ");
    long insertId = database.insert(DBController.TABLE_ASSIGNMENTS, null,
        values);
    Cursor cursor = database.query(DBController.TABLE_ASSIGNMENTS,
        allColumns, DBController.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Assignment newAssignment = cursorToAssignment(cursor);
    cursor.close();
    return newAssignment;
  }

  public void deleteComment(Assignment assignment) {
    long id = assignment.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(DBController.TABLE_ASSIGNMENTS, DBController.COLUMN_ID
        + " = " + id, null);
  }

  public List<Assignment> getAllAssignments() {
    List<Assignment> assignments = new ArrayList<Assignment>();

    Cursor cursor = database.query(DBController.TABLE_ASSIGNMENTS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Assignment assignment = cursorToAssignment(cursor);
      assignments.add(assignment);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return assignments;
  }

  private Assignment cursorToAssignment(Cursor cursor) {
	Assignment assignment = new Assignment();
	assignment.setId(cursor.getLong(0));
	assignment.setComment(cursor.getString(1));
    return assignment;
  }
} 
