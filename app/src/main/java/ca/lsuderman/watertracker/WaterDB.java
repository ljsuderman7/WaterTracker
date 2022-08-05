package ca.lsuderman.watertracker;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WaterDB extends Application {
    private static final String DB_NAME = "db_Water";
    private static int DB_VERSION = 1;

    private SQLiteOpenHelper helper;

    @Override
    public void onCreate() {
        helper = new SQLiteOpenHelper(this, DB_NAME, null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS tbl_cups(" +
                        "CupID INTEGER PRIMARY KEY," +
                        "IsDone INTEGER NOT NULL)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                //no-op
            }
        };

        super.onCreate();
    }

    // Add Cup
    public void addCup(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO tbl_cups(IsDone) VALUES (0)");
    }

    // Finish Cup (UPDATE)
    public void finishCup(int CupID){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE tbl_cups SET IsDone = 1 WHERE CupID = " + CupID);
    }

    // Reset Cup (UPDATE)
    public void resetCup(int CupID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE tbl_cups SET IsDone = 0 WHERE CupID = " + CupID);
    }

    // Reset All Cups


    //Get Cup
    public Cup getCup(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_cups WHERE CupID = " + id, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Cup cup = new Cup();
        cup.setCupID(cursor.getInt(0));
        // set isDone based on if it is 0 (True), or 1 (false)
        int isDoneInt = cursor.getInt(1);
        if(isDoneInt == 0){
            cup.setIsDone(true);
        }
        else{
            cup.setIsDone(false);
        }
        cursor.close();
        return cup;
    }

    //Get All Cups
    public List<Cup> getAllCups(){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Cup> cups = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_cups ORDER BY CupID", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            while (cursor.getPosition() < cursor.getCount()) {
                Cup cup = new Cup();
                cup.setCupID(cursor.getInt(0));

                // set isDone based on if it is 0 (True), or 1 (false)
                int isDoneInt = cursor.getInt(1);
                if(isDoneInt == 0){
                    cup.setIsDone(true);
                }
                else{
                    cup.setIsDone(false);
                }

                cups.add(cup);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return cups;
    }
}



























