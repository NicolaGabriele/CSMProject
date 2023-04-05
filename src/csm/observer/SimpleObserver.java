package csm.observer;

public class SimpleObserver implements StationObserver{

    private double endTime, busyTime, serviceTime;
    private int numberOfService, arrivi;

    public SimpleObserver(double endTime){
        this.endTime = endTime;
        this.busyTime = 0;
        this.numberOfService = 0;
        this.serviceTime = 0;
        this.arrivi = 0;
    }

    @Override
    public void notifyBusyTime(double time) {
        busyTime += time;
        numberOfService++;
    }

    public double getBusyTime() {
        return busyTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public int getServices(){
        return numberOfService;
    }

    @Override
    public void notifyServiceTime(double time){
        serviceTime += time;
    }

    public double getSoujornTime(){
        return busyTime/numberOfService;
    }

    public double getThroughput(){
        return numberOfService/endTime;
    }

    public double getUtilizzation(){
        return serviceTime/endTime;
    }

    public void setServiceTime(double time){
        this.serviceTime = time;
    }

    public int getNumberOfService() {
        return numberOfService;
    }

    public int getArrivi(){
        return arrivi;
    }

    @Override
    public void incrementArrival(){
        arrivi++;
    }
}
