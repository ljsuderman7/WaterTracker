package ca.lsuderman.watertracker;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity {

    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btnReset, btnPreferences;
    private ImageButton btnCup1, btnCup2, btnCup3, btnCup4, btnCup5, btnCup6, btnCup7, btnCup8;
    private LinearLayout llCups;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertDummyData();

//        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
//        toast.show();

        long delay = Utilities.getInitialDelay(2);

        // Creates a PeriodicWorkRequest that will repeat everyday, and is initially delayed until 2AM
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(EverydayWorker.class, 1, TimeUnit.DAYS)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

        // Puts request in the queue
        WorkManager.getInstance().enqueueUniquePeriodicWork("reset_cups_periodic",
                ExistingPeriodicWorkPolicy.REPLACE,
                request);

        //region Image Cup Buttons
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

        // Reset Button
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((WaterDB) getApplicationContext()).resetAllCups();
                } catch (Exception ex) {
                    // no-op
                }
                setCups();
            }
        });
        
        // Preferences Button
        btnPreferences = findViewById(R.id.btnPreferences);
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
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

        if (results == null || results.isEmpty()) {
            try {
                ((WaterDB) getApplication()).addResult("2022-07-31", 1);
                ((WaterDB) getApplication()).addResult("2022-08-01", 1);
                ((WaterDB) getApplication()).addResult("2022-08-02", 0);
                ((WaterDB) getApplication()).addResult("2022-08-03", 1);
                ((WaterDB) getApplication()).addResult("2022-08-04", 0);
                ((WaterDB) getApplication()).addResult("2022-08-05", 0);
                ((WaterDB) getApplication()).addResult("2022-08-06", 1);
                ((WaterDB) getApplication()).addResult("2022-08-07", 1);
                ((WaterDB) getApplication()).addResult("2022-08-08", 0);
                ((WaterDB) getApplication()).addResult("2022-08-09", 1);
            } catch (Exception ex){
                // no-op
            }
        }

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
}