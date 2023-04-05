package csm.Client;

public class Client {
    private int id;
    private double startWaitingTime, finalTime, globalStart, globalEnd;

    public Client(int id){
        this.id = id;
        this.startWaitingTime = 0;
    }

    public void setGlobalStart(double gs){
        this.globalStart = gs;
    }

    public void setGlobalEnd(double ge){
        this.globalEnd = ge;
    }

    public double getGlobalEnd() {
        return globalEnd;
    }

    public double getGlobalStart() {
        return globalStart;
    }

    public int getId(){
        return this.id;
    }

    public void setStartWaitingTime(double startTime) {
        this.startWaitingTime = startTime;
    }

    public double getStartWaitingTime(){
        return startWaitingTime;
    }

    public void setFinalTime(double time){finalTime = time;}

    public double getFinalTime(){return  finalTime;}
}
