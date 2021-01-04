package com.example.thevoice;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    String name;
    String surname;
    private int point ;
    private Score s;

    //for the decibels
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    //permission of recording an audio
    private static final int RECORD_AUDIO_PERMISSION_REQUEST = 1;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    //for the debug
    private static final String TAG = "LocRec";
    private static final String TAG_N = "NOM";
    private static final String TAG_P = "PRENOM";
    private static final String TAG_SCORE = "SCORE CALCULE";
    private static final String TAG_CALCUL = "CALCUL";

    //Textview for the player's name
    private TextView textViewName;

    //for the recorder
    private MediaRecorder recorder = null;

    private File audioFile;
    private ImageButton imageButton;

    //for the database
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Get intent with extras
        Bundle intent = getIntent().getExtras();
        name = intent.getString(MainActivity.NOM);
        surname = intent.getString(MainActivity.PRENOM);

        Log.i(TAG_N, name);
        Log.i(TAG_P, surname);

        //print the name
        textViewName = findViewById(R.id.message5);
        textViewName.setText("Hi " + name + " ! Click the mic to record your audio !");

        //calling the click method with the button
        imageButton = (ImageButton) findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(this);

        //creating the audio file
        audioFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "audio_test4.3gp");

        Log.i(TAG, audioFile.getAbsolutePath());
    }

    // this process must be done prior to the start of recording
    private void resetRecorder() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncodingBitRate(16);
        recorder.setAudioSamplingRate(44100);
        recorder.setOutputFile(audioFile.getAbsolutePath());

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Calcul des déciBel
    private double soundDb(double ampl){
        return  20 * Math.log10( getAmplitudeEMA() / ampl);
    }

    //physics calcul
    private double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        if(amp == 0){
            amp = 1 ;
        }
        return amp;
    }

    //getAmplitude of max the recorder
    private double getAmplitude(){
        if(recorder != null){
            int ampl = recorder.getMaxAmplitude();
            Log.i(TAG_CALCUL, Integer.toString(ampl));
            return ampl;
        }
        else
            return 0;
    }

    //clicking the mic button
    public void onClick(View v) {
        int delay = 2000;
        recorder = new MediaRecorder();
        Toast.makeText(this, "AUDIO RECORDING", Toast.LENGTH_SHORT).show();
        resetRecorder();
        recorder.start();
        recorder.getMaxAmplitude();

        imageButton.setEnabled(false);

        try {
            Thread.sleep(delay);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        point = (int) soundDb(10 * Math.exp(-5));
        Log.i(TAG_SCORE, Integer.toString(point) + " dB");

        db = new DatabaseHandler(this);
        db.addRow(new Score(db.getCurrentId()+1, name, surname, point));

        Toast.makeText(this, "AUDIO RECORDED", Toast.LENGTH_SHORT).show();
        recorder.stop();
        recorder.release();
        recorder = null;

        imageButton.setEnabled(true);

        Intent i = new Intent(this, ScoreActivity.class);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    //asking for the audio permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode){
            case RECORD_AUDIO_PERMISSION_REQUEST:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted){
            finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    public void previous(View view) {
        // Create an intent for the second activity
        Intent intent = new Intent(this, MainActivity.class);

        // Start the activity
        startActivity(intent);
    }

    public void done(View view) {
        // Create an intent for the activity
        Intent i = new Intent(this, ScoreActivity.class);

        // Start the activity
        startActivity(i);
    }
}