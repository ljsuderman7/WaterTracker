package ca.lsuderman.watertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnCup1, btnCup2, btnCup3, btnCup4, btnCup5, btnCup6, btnCup7, btnCup8;
    private LinearLayout llCups;
    private TextView txtDaysComplete, txtDaysIncomplete, txtPercentage, txtDaysCompleteLabel, txtDaysIncompleteLabel, txtDrinkingHistoryLabel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: -previous weeks results display at bottom of home page
        //      -daily progress bar on home page
        //      -graphs to track results
        //      -different sized cups
        //          -switch to ml per day, not just cups
        //      -better UI

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("Home");
        }

        insertDummyData();

        List<DailyResult> results = null;
        try {
            results = ((WaterDB) getApplication()).getAllResults();
        } catch (Exception ex){
            // no-op
        }

        // Creates a OneTimeWorkRequest the first time the app is opened
        if (results.isEmpty()) {
            Log.d("Results is Empty", String.valueOf(results.isEmpty()));
            WorkRequest initialLoadRequest = new OneTimeWorkRequest.Builder(EverydayWorker.class)
                    .setInputData(
                            new Data.Builder()
                                    .putBoolean("initialLoad", true)
                                    .build()
                    )
                    .addTag("initial_load_request")
                    .build();
            WorkManager.getInstance().enqueue(initialLoadRequest);
        }

        long delay = Utilities.getInitialDelay(2);

        // Creates a PeriodicWorkRequest that will repeat everyday, with an initial delay until 2AM
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(EverydayWorker.class, 1, TimeUnit.DAYS)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInputData(
                                new Data.Builder()
                                        .putBoolean("initialLoad", false)
                                        .build()
                        )
                        .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork("everyday_request",
                ExistingPeriodicWorkPolicy.KEEP,
                request);

        //region Image Cup Buttons Setup

        Toast notCurrentCup = Toast.makeText(getApplicationContext(), "Not Current Cup", Toast.LENGTH_SHORT);
        llCups = findViewById(R.id.llCups);
        btnCup1 = findViewById(R.id.btnCup1);
        btnCup2 = findViewById(R.id.btnCup2);
        btnCup3 = findViewById(R.id.btnCup3);
        btnCup4 = findViewById(R.id.btnCup4);
        btnCup5 = findViewById(R.id.btnCup5);
        btnCup6 = findViewById(R.id.btnCup6);
        btnCup7 = findViewById(R.id.btnCup7);
        btnCup8 = findViewById(R.id.btnCup8);

        llCups.post(new Runnable() {
            @Override
            public void run() {
                setCups();
            }
        });

        btnCup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCupImage(btnCup1);
                try {
                    ((WaterDB) getApplication()).finishCup(1);
                } catch (Exception ex) {
                    // no-op
                }
            }
        });

        btnCup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(1)) {
                    changeCupImage(btnCup2);
                    try {
                        ((WaterDB) getApplication()).finishCup(2);
                    } catch (Exception ex) {
                        // no-op
                    }
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btnCup3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(2)) {
                    changeCupImage(btnCup3);
                    try {
                        ((WaterDB) getApplication()).finishCup(3);
                    } catch (Exception ex) {
                        // no-op
                    }
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btnCup4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(3)) {
                    changeCupImage(btnCup4);
                    try {
                        ((WaterDB) getApplication()).finishCup(4);
                    } catch (Exception ex) {
                        // no-op
                    }
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btnCup5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(4)) {
                    changeCupImage(btnCup5);
                    try {
                        ((WaterDB) getApplication()).finishCup(5);
                    } catch (Exception ex) {
                        // no-op
                    }
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btnCup6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(5)) {
                    changeCupImage(btnCup6);
                    try {
                        ((WaterDB) getApplication()).finishCup(6);
                    } catch (Exception ex) {
                        // no-op
                    }
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btnCup7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(6)) {
                    changeCupImage(btnCup7);
                    try {
                        ((WaterDB) getApplication()).finishCup(7);
                    } catch (Exception ex) {
                        // no-op
                    }
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btnCup8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(7)) {
                    changeCupImage(btnCup8);
                    try {
                        ((WaterDB) getApplication()).finishCup(8);
                    } catch (Exception ex) {
                        // no-op
                    }
                } else {
                    notCurrentCup.show();
                }
            }
        });

        //endregion

        //region Daily Results TextViews

        txtDaysComplete = findViewById(R.id.txtDaysCompleted);
        txtDaysIncomplete = findViewById(R.id.txtDaysIncomplete);
        txtPercentage = findViewById(R.id.txtPercentage);
        txtDaysCompleteLabel = findViewById(R.id.txtDaysCompleteLabel);
        txtDaysIncompleteLabel = findViewById(R.id.txtDaysIncompleteLabel);
        txtDrinkingHistoryLabel = findViewById(R.id.txtDrinkingHistoryLabel);

        double completeDays = 0;
        double incompleteDays = 0;
        for (DailyResult result:  results) {
            if (result.getFinishedAllCups()){
                completeDays++;
            } else {
                incompleteDays++;
            }
        }

        if (results.size() > 0) {
            txtDaysCompleteLabel.setVisibility(View.VISIBLE);
            txtDaysIncompleteLabel.setVisibility(View.VISIBLE);
            txtDrinkingHistoryLabel.setVisibility(View.VISIBLE);

            txtDaysComplete.setText(String.valueOf((int) completeDays));
            txtDaysIncomplete.setText(String.valueOf((int) incompleteDays));

            double totalDays = completeDays + incompleteDays;
            double completedPercentage = completeDays / totalDays;
            double displayPercentage = completedPercentage * 100;
            String percentageText = "You've had the recommended amount of water " + (int) displayPercentage + "% of the time. ";

            if (completedPercentage >= 0.90) {
                percentageText += "Keep up the good work!";
                txtPercentage.setTextColor(getResources().getColor(R.color.green));
            } else if (completedPercentage >= 0.75) {
                percentageText += "Good job but you can do a little better.";
                txtPercentage.setTextColor(getResources().getColor(R.color.green));
            } else if (completedPercentage >= 0.5) {
                percentageText += "You can do better.";
                txtPercentage.setTextColor(getResources().getColor(R.color.yellow));
            } else if (completedPercentage >= 0.25) {
                percentageText += "You can do a lot better.";
                txtPercentage.setTextColor(getResources().getColor(R.color.orange));
            } else {
                percentageText += "You need to drink more water!";
                txtPercentage.setTextColor(getResources().getColor(R.color.red));
            }

            txtPercentage.setText(percentageText);
        }


        //endregion
    }

    private void setCups() {
        List<Cup> cups = null;
        try {
            cups = ((WaterDB) getApplication()).getAllCups();
        } catch (Exception ex) {
            // no-op
        }

        if (!cups.isEmpty()) {
            for (Cup cup : cups) {
                Bitmap bmp = createCupBitmap(cup.getIsDone());
                switch (cup.getCupID()) {
                    case 1:
                        btnCup1.setImageBitmap(bmp);
                        btnCup1.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case 2:
                        btnCup2.setImageBitmap(bmp);
                        btnCup2.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case 3:
                        btnCup3.setImageBitmap(bmp);
                        btnCup3.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case 4:
                        btnCup4.setImageBitmap(bmp);
                        btnCup4.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case 5:
                        btnCup5.setImageBitmap(bmp);
                        btnCup5.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case 6:
                        btnCup6.setImageBitmap(bmp);
                        btnCup6.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case 7:
                        btnCup7.setImageBitmap(bmp);
                        btnCup7.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case 8:
                        btnCup8.setImageBitmap(bmp);
                        btnCup8.setBackgroundColor(Color.TRANSPARENT);
                        break;
                }
            }
        }
    }

    private boolean isCurrentCup(int cupId){
        boolean currentCup = false;
        Cup cup = new Cup();
        try {
            cup = ((WaterDB) getApplication()).getCup(cupId);
        } catch (Exception ex){
            // no-op
        }

        if(cup.getIsDone()){
            currentCup = true;
        }
        return currentCup;
    }

    private void changeCupImage(ImageButton button){
        llCups.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = createCupBitmap(true);

                button.setImageBitmap(bmp);
                button.setBackgroundColor(Color.TRANSPARENT);
            }
        });
    }

    private Bitmap createCupBitmap(boolean isFinished){
        Bitmap bmp;
        int layoutWidth = llCups.getWidth();
        int width = layoutWidth / 16;

        //Log.d("Layout Width", String.valueOf(layoutWidth));

//        boolean tooBig = true;
//        int i = 1;
//        do {
//            width = layoutWidth / i;
//            if (width * 8 < layoutWidth){
//                tooBig = false;
//            }
//            i++;
//            //Log.d("Width", String.valueOf(width * 8));
//        } while(tooBig);

        double divider = (double)getDrawable(R.drawable.cup).getIntrinsicWidth() / width;
        double height = getDrawable(R.drawable.cup).getIntrinsicHeight() / divider;

        if (isFinished) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.finished_cup);
        } else {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cup);
        }
        bmp = Bitmap.createScaledBitmap(bmp, width, (int)height, true);

//        Log.d("Bitmap Width", String.valueOf(bmp.getWidth()));
//        Log.d("Bitmap Height", String.valueOf(bmp.getHeight()));

        return bmp;
    }

    private void insertDummyData(){
        List<DailyResult> results = ((WaterDB) getApplication()).getAllResults();
        List<Cup> cups = ((WaterDB) getApplication()).getAllCups();

//        if (results == null || results.isEmpty()) {
//            try {
//                ((WaterDB) getApplication()).addResult("2022-07-31", 1);
//                ((WaterDB) getApplication()).addResult("2022-08-01", 1);
//                ((WaterDB) getApplication()).addResult("2022-08-02", 0);
//                ((WaterDB) getApplication()).addResult("2022-08-03", 1);
//                ((WaterDB) getApplication()).addResult("2022-08-04", 0);
//                ((WaterDB) getApplication()).addResult("2022-08-05", 0);
//                ((WaterDB) getApplication()).addResult("2022-08-06", 1);
//                ((WaterDB) getApplication()).addResult("2022-08-07", 1);
//                ((WaterDB) getApplication()).addResult("2022-08-08", 0);
//                ((WaterDB) getApplication()).addResult("2022-08-09", 1);
//            } catch (Exception ex){
//                // no-op
//            }
//        }

        if (cups == null || cups.isEmpty()){
            for (int i = 0; i < 8; i++){
                try {
                    ((WaterDB) getApplication()).addCup();
                } catch (Exception ex){
                    // no-op
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = false;
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            default:
                ret = super.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}