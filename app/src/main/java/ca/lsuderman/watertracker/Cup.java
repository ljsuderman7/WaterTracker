package ca.lsuderman.watertracker;

public class Cup {
    private int CupID;
    private boolean IsDone;

    public int getCupID(){
        return CupID;
    }
    public void setCupID(int CupID){
        this.CupID=CupID;
    }

    public boolean getIsDone(){
        return IsDone;
    }
    public void setIsDone(boolean IsDone){
        this.IsDone = IsDone;
    }

    public Cup(int CupID, boolean IsDone){
        this.CupID = CupID;
        this.IsDone = IsDone;
    }
    public Cup(){
        // no-op
    }
}
