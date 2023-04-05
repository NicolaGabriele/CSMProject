package csm.alternative;

import csm.Client.Client;
import csm.observer.StationObserver;
import smart.theatre.distributions.Distribution;
import smart.theatre.standalone.Actor;

import java.util.LinkedList;

public abstract class Station extends Actor {

    private Distribution distribution;
    private LinkedList<Client> waitingLine = new LinkedList<>();

    protected Station[] stations;

    private int numServers, inService = 0, inWaiting = 0;
    private StationObserver observer;

    private double start = 0;


    public abstract void arrival(Client c);
    public abstract void departure(Client c);


    protected void setDistribution(Distribution d){
        this.distribution = d;
    }

    protected void setObserver(StationObserver observer){
        this.observer = observer;
    }

    protected StationObserver observer(){
        return observer;
    }

    protected void setServersNumber(int num){
        this.numServers = num;
    }

    protected void addToWaitingLine(Client c){
        waitingLine.addLast(c);inWaiting++;
    }

    protected Client nextClient(){
        if(waitingLine.size()>0)inWaiting--;
        return (waitingLine.size()>0)?waitingLine.removeFirst():null;
    }

    protected void incrementInService(){
        inService++;
    }

    protected void decrementInService(){
        inService--;
    }

    protected int inService(){
        return inService;
    }

    protected int serversNumber(){
        return numServers;
    }

    protected Distribution probabilityDistribution(){return distribution;}

    protected int inWaitingLine(){return waitingLine.size();}

    protected void decrementInWaiting(){
        inWaiting--;
    }


}//Station
