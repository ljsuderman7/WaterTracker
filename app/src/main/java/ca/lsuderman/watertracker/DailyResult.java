package ca.lsuderman.watertracker;

public class DailyResult {
    private int ResultID;
    private String Date;
    private boolean FinishedAllCups;

    public int getResultID(){
        return ResultID;
    }
    public void setResultID(int ResultID){
        this.ResultID=ResultID;
    }

    public String getDate(){
        return Date;
    }
    public void setDate(String Date){
        this.Date = Date;
    }

    public boolean getFinishedAllCups(){
        return FinishedAllCups;
    }
    public void setFinishedAllCups(boolean FinishedAllCups){
        this.FinishedAllCups = FinishedAllCups;
    }

    public DailyResult(int ResultID, String Date, boolean FinishedAllCups){
        this.ResultID = ResultID;
        this.Date = Date;
        this.FinishedAllCups = FinishedAllCups;
    }
    public DailyResult(){
        // no-op
    }
}
