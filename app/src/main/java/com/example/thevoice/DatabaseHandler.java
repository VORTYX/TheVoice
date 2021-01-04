package com.example.thevoice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database version
    private static final int DATABASE_VERSION = 6;
    // Database name
    private static final String DATABASE_NAME = "scoreManager";
    // Table name
    private static final String TABLE_SCORES = "score";
    // Table columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SUR = "surname";
    private static final String KEY_DB = "dB";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the table
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SUR + " TEXT," + KEY_DB + " INTEGER )";
        db.execSQL(CREATE_SCORES_TABLE);

    }

    // Upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        // Create tables again
        onCreate(db);
    }

    // Add a new row
    void addRow(Score s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, s.getName());
        values.put(KEY_SUR, s.getSurname());
        values.put(KEY_DB, s.getdB());
        // Insert row
        db.insert(TABLE_SCORES, null, values);
        db.close();
    }

    // Get all rows ordered by score
    public List<Score> getAllRows() {
        List<Score> l = new ArrayList<>();

        // Select all query
        String selectQuery = "SELECT * FROM " + TABLE_SCORES + " ORDER BY " + KEY_DB + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all the rows and addi the to the list
        if (cursor.moveToFirst()) {
            do {
                Score s = new Score();
                s.setID(Integer.parseInt(cursor.getString(0)));
                s.setName(cursor.getString(1));
                s.setSurname(cursor.getString(2));
                s.setdB(cursor.getInt(3));
                // Add row to list
                l.add(s);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // Return the list
        return l;
    }

    // Clear the table
    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SCORES);
        db.close();
    }

    public int getCurrentId() {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT MAX(" + KEY_ID + ") FROM " + TABLE_SCORES;
        Cursor c = db.rawQuery(q, null);

        if (c.moveToFirst()) {
            int id = c.getInt(0);
            c.close();
            db.close();
            return id;
        } else {
            return 0;
        }
    }
}
