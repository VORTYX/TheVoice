package com.example.thevoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    //Name of the activity
    public static String TAG = "ScoreActivity";

    //Permission to write external storage (database)
    public static final int PERMISSIONS_REQUEST = 0;

    //for the intent
    public static final String INTENT_FILTER = "filter";

    //for the file
    public static String SCORES_FILE_PATH = "";

    //for the database
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        db = new DatabaseHandler(this);

        //lines to clear the database
        //db.clear();

        //check the permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //if permission is already ok
            printScores();
            listScores();
        } else {
            //asking for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
            Toast.makeText(this, "Permission not accepted yet", Toast.LENGTH_SHORT).show();
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    //checking for the results of permissions
    public void onRequestPermissionsResult(int request, String permissions[], int[] results) {
        switch (request) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission OK", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission denied
                    Toast.makeText(this, "Permission denied to access device's storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    // Log for db sql
    private void printScores() {
        Log.d(TAG, "Printing...");
        List<Score> scores = db.getAllRows();
        for (Score e : scores) {
            String log = "ID: " + e.getID() + ", Name: " + e.getName() + ", Surname: " +
                    e.getSurname() + ", dB : " + e.getdB();
            Log.d(TAG, log);
        }
    }

    //add in ListView
    private void listScores() {
        Log.d(TAG, "Listing...");
        // Get list from database
        final List<Score> scores = db.getAllRows();
        // Copy to new list
        List<String> list = new ArrayList<>();

        for (Score e : scores) {
            list.add("Nom : " + e.getName() + " / Score : " + e.getdB());
        }
        // Create ListView
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<>(ScoreActivity.this, android.R.layout.simple_list_item_1, list));
    }

    //home page
    public void previous(View view) {
        // Create an intent for the second activity
        Intent intent = new Intent(this, MainActivity.class);

        // Start the activity
        startActivity(intent);
    }
}