package ca.lsuderman.watertracker;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btnReset;
    private ImageButton btnCup1, btnCup2;
    private LinearLayout llCups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Insert new cups (debug)

//        for (int i = 0; i < 8; i++){
//            ((WaterDB) getApplication()).addCup();
//        }

        //REMOVE LATER
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
                btn2.setEnabled(false);
                ((WaterDB) getApplication()).finishCup(2);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn3.setEnabled(false);
                ((WaterDB) getApplication()).finishCup(3);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn4.setEnabled(false);
                ((WaterDB) getApplication()).finishCup(4);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn5.setEnabled(false);
                ((WaterDB) getApplication()).finishCup(5);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn6.setEnabled(false);
                ((WaterDB) getApplication()).finishCup(6);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn7.setEnabled(false);
                ((WaterDB) getApplication()).finishCup(7);
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn8.setEnabled(false);
                ((WaterDB) getApplication()).finishCup(8);
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
}