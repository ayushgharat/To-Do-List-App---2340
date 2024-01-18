package com.example.project1.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClassDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ClassDatabase";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CLASSES = "classes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CLASS_NAME = "class_name";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_INSTRUCTOR_NAME = "instructor_name";

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_CLASSES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CLASS_NAME + " TEXT NOT NULL, " +
                    COLUMN_START_TIME + " TEXT NOT NULL, " +
                    COLUMN_END_TIME + " TEXT NOT NULL, " +
                    COLUMN_INSTRUCTOR_NAME + " TEXT NOT NULL);";

    public ClassDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        onCreate(db);
    }

    // Method to query all classes from the database
    public Cursor getAllClasses() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_CLASSES, null, null, null, null, null, null);
    }

    public Cursor getClassById(long classId) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(classId)};
        return db.query(TABLE_CLASSES, null, selection, selectionArgs, null, null, null);
    }

    public void deleteClass(long classId) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(classId)};
        db.delete(TABLE_CLASSES, selection, selectionArgs);
        db.close();
    }
}
