package csm.observer;

public interface StationObserver {

    void notifyBusyTime(double time);
    void notifyServiceTime(double time);

    void incrementArrival();

    double getBusyTime();
    double getServiceTime();
    int getServices();

    void setServiceTime(double time);

}//StationObserver
