package com.example.thevoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.AlarmManager;
import android.app.PendingIntent;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    //Extra tag
    public static final String NOM = "Nom";
    public static final String PRENOM = "Prenom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create notif display each day at calendar time
        myNotif();
    }

    //Go to game !
    public void start(View view) {
        Intent intent = new Intent(this, SecondActivity.class);

        EditText eTname = findViewById(R.id.name);
        EditText eTsurname = findViewById(R.id.surname);
        String name = eTname.getText().toString();
        String surname = eTsurname.getText().toString();

        if (name.trim().equals("")) {
            eTname.setError("You have to specify your name");
            eTname.setHint("Please enter your name");
        } else if (surname.trim().equals("")) {
            eTsurname.setError("You have to specify your surname");
            eTsurname.setHint("Please enter your surname");
        } else {
            intent.putExtra(NOM, name);
            intent.putExtra(PRENOM, surname);
            startActivity(intent);
        }
    }

    //fonction notif which permit display notif
    public void myNotif(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 46);
        calendar.set(calendar.SECOND, 0);

        if(calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    //Go to the scoreboard
    public void score(View view) {
        Intent i = new Intent(this, ScoreActivity.class);
        startActivity(i);
    }
}