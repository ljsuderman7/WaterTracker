package ca.lsuderman.watertracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
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
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btnReset;
    private ImageButton btnCup1, btnCup2;
    private LinearLayout llCups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int hourOfTheDay = 2;
        int repeatInterval = 1;

        Calendar desiredTime = Calendar.getInstance();
        desiredTime.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
        desiredTime.set(Calendar.MINUTE, 0);
        desiredTime.set(Calendar.SECOND, 0);

        Calendar currentTime = Calendar.getInstance();

        long delay = currentTime.getTimeInMillis() - desiredTime.getTimeInMillis();

        Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(delay), Toast.LENGTH_LONG);
        toast.show();

        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(EverydayWorker.class,
                        repeatInterval,
                        TimeUnit.DAYS)
                        /*.setInitialDelay(delay, TimeUnit.MILLISECONDS)*/
                        .build();

        //TODO: Make a better tag
        WorkManager.getInstance().enqueueUniquePeriodicWork("reset_cups_periodic",
                ExistingPeriodicWorkPolicy.REPLACE,
                request);

//        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(EverydayWorker.class,
//                repeatInterval,
//                TimeUnit.DAYS)
//                .setInitialDelay(delay);

        // runs during the last 15 minutes of every hour
//        WorkRequest saveRequest =
//                new PeriodicWorkRequest.Builder(SaveImageToFileWorker.class,
//                        1, TimeUnit.HOURS,
//                        15, TimeUnit.MINUTES)
//                        .build();

        //region Cup Buttons

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);

        setCups();

        Toast notCurrentCup = Toast.makeText(getApplicationContext(), "Not Current Cup", Toast.LENGTH_LONG);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setEnabled(false);
                ((WaterDB) getApplication()).finishCup(1);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(btn1)){
                    btn2.setEnabled(false);
                    ((WaterDB) getApplication()).finishCup(2);
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(btn2)){
                    btn3.setEnabled(false);
                    ((WaterDB) getApplication()).finishCup(3);
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(btn3)){
                    btn4.setEnabled(false);
                    ((WaterDB) getApplication()).finishCup(4);
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(btn4)){
                    btn5.setEnabled(false);
                    ((WaterDB) getApplication()).finishCup(5);
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(btn5)){
                    btn6.setEnabled(false);
                    ((WaterDB) getApplication()).finishCup(6);
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(btn6)){
                    btn7.setEnabled(false);
                    ((WaterDB) getApplication()).finishCup(7);
                } else {
                    notCurrentCup.show();
                }
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentCup(btn7)){
                    btn8.setEnabled(false);
                    ((WaterDB) getApplication()).finishCup(8);
                } else {
                    notCurrentCup.show();
                }
            }
        });
        //endregion

        //region Image Cup Buttons
        llCups = findViewById(R.id.llCups);

//        int height = getDrawable(R.drawable.cup).getIntrinsicHeight() / 2;
//        int width = getDrawable(R.drawable.cup).getIntrinsicWidth() / 2;

        btnCup1 = findViewById(R.id.btnCup1);
        btnCup2 = findViewById(R.id.btnCup2);

        llCups.post(new Runnable() {
            @Override
            public void run() {
                int layoutWidth = llCups.getWidth();

                int width = llCups.getWidth() / 8; //change to number of cups from 8...

                int divider = getDrawable(R.drawable.cup).getIntrinsicWidth() / width;

                int height = getDrawable(R.drawable.cup).getIntrinsicWidth() / divider;

                Bitmap bmp;
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cup);
                bmp = Bitmap.createScaledBitmap(bmp, width, height, true);


                btnCup1.setImageBitmap(bmp);
                btnCup1.setBackgroundColor(Color.TRANSPARENT);

                btnCup2.setImageBitmap(bmp);
                btnCup2.setBackgroundColor(Color.TRANSPARENT);

//                Log.d("Layout Width",String.valueOf(llCups.getWidth()));
//                Log.d("Button Width",String.valueOf(btnCup1.getWidth()));
//                Log.d("Divi",String.valueOf(width));
                //Log.d("Layout Width",String.valueOf(width));
            }
        });

//        Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(), Toast.LENGTH_LONG);
//        toast.show();


//        Bitmap bmp;
//        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cup);
//        bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
//
//        btnCup1 = findViewById(R.id.btnCup1);
//
//        btnCup1.setImageBitmap(bmp);
//        btnCup1.setBackgroundColor(Color.TRANSPARENT);

        btnCup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCup1.setBackgroundResource(R.drawable.finished_cup);
            }
        });

        //endregion


        // Reset Button
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i <= 8; i++) {
                    ((WaterDB) getApplication()).resetCup(i);
                    setCups();
                }
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
                switch (cup.getCupID()) {
                    case 1:
                        if (!cup.getIsDone()) {
                            btn1.setEnabled(false);
                        } else {
                            btn1.setEnabled(true);
                        }
                        break;
                    case 2:
                        if (!cup.getIsDone()) {
                            btn2.setEnabled(false);
                        } else {
                            btn2.setEnabled(true);
                        }
                        break;
                    case 3:
                        if (!cup.getIsDone()) {
                            btn3.setEnabled(false);
                        } else {
                            btn3.setEnabled(true);
                        }
                        break;
                    case 4:
                        if (!cup.getIsDone()) {
                            btn4.setEnabled(false);
                        } else {
                            btn4.setEnabled(true);
                        }
                        break;
                    case 5:
                        if (!cup.getIsDone()) {
                            btn5.setEnabled(false);
                        } else {
                            btn5.setEnabled(true);
                        }
                        break;
                    case 6:
                        if (!cup.getIsDone()) {
                            btn6.setEnabled(false);
                        } else {
                            btn6.setEnabled(true);
                        }
                        break;
                    case 7:
                        if (!cup.getIsDone()) {
                            btn7.setEnabled(false);
                        } else {
                            btn7.setEnabled(true);
                        }
                        break;
                    case 8:
                        if (!cup.getIsDone()) {
                            btn8.setEnabled(false);
                        } else {
                            btn8.setEnabled(true);
                        }
                        break;
                }
            }
        }

    }

    private boolean isCurrentCup(Button button){
        boolean currentCup = false;
        if(!button.isEnabled()){
            currentCup = true;
        }
        return currentCup;
    }
}