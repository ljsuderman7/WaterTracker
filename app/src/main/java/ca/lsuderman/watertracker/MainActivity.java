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
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
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
        
//        // Preferences Button
//        btnPreferences = findViewById(R.id.btnPreferences);
//        btnPreferences.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
//            }
//        });
    }

    private void setCups() {
        List<Cup> cups = null;
        try {
            cups = ((WaterDB) getApplication()).getAllCups();
        } catch (Exception ex) {
            // no-op
        }

        // Buttons
//        if (!cups.isEmpty()) {
//            for (Cup cup : cups) {
//                switch (cup.getCupID()) {
//                    case 1:
//                        if (cup.getIsDone()) {
//                            btn1.setEnabled(false);
//                        } else {
//                            btn1.setEnabled(true);
//                        }
//                        break;
//                    case 2:
//                        if (cup.getIsDone()) {
//                            btn2.setEnabled(false);
//                        } else {
//                            btn2.setEnabled(true);
//                        }
//                        break;
//                    case 3:
//                        if (cup.getIsDone()) {
//                            btn3.setEnabled(false);
//                        } else {
//                            btn3.setEnabled(true);
//                        }
//                        break;
//                    case 4:
//                        if (cup.getIsDone()) {
//                            btn4.setEnabled(false);
//                        } else {
//                            btn4.setEnabled(true);
//                        }
//                        break;
//                    case 5:
//                        if (cup.getIsDone()) {
//                            btn5.setEnabled(false);
//                        } else {
//                            btn5.setEnabled(true);
//                        }
//                        break;
//                    case 6:
//                        if (cup.getIsDone()) {
//                            btn6.setEnabled(false);
//                        } else {
//                            btn6.setEnabled(true);
//                        }
//                        break;
//                    case 7:
//                        if (cup.getIsDone()) {
//                            btn7.setEnabled(false);
//                        } else {
//                            btn7.setEnabled(true);
//                        }
//                        break;
//                    case 8:
//                        if (cup.getIsDone()) {
//                            btn8.setEnabled(false);
//                        } else {
//                            btn8.setEnabled(true);
//                        }
//                        break;
//                }
//            }
//        }

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

    private boolean isCurrentCup(Button button){
        boolean currentCup = false;
        if(!button.isEnabled()){
            currentCup = true;
        }
        return currentCup;
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
}