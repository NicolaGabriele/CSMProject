package csm.alternative;

import csm.Client.Client;
import csm.Main;
import csm.observer.StationObserver;
import smart.theatre.distributions.Distribution;

public class KServerStation extends Station{


    private double start;
    private int id;
    @Msgsrv
    public void init(Distribution d, Integer numServers, Integer id, Station[] stations, StationObserver observer){
        setDistribution(d);
        this.id = id;
        setServersNumber(numServers);
        this.stations = stations;
        setObserver(observer);
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        c.setStartWaitingTime(now());
        if(inService() < serversNumber()){
            if(inService() == 0)start = now();
            if(id == 1)
                Main.path.up(now());
            send(probabilityDistribution().nextSample(), "departure",c);
            incrementInService();
        }else{
            addToWaitingLine(c);
        }
    }

    @Override @Msgsrv
    public void departure(Client c) {
        c.setFinalTime(now());
        observer().incrementArrival();
        observer().notifyBusyTime(c.getFinalTime()-c.getStartWaitingTime());
        decrementInService();
        stations[0].send("arrival",c);
        if(inWaitingLine() == 0 && inService() == 0)
            observer().notifyServiceTime(now()-start);
        else{
            Client next = nextClient();
            decrementInWaiting();
            if(next != null) {
                if(id == 1)Main.path.up(now());
                send(probabilityDistribution().nextSample(), "departure", next);
                incrementInService();
            } 
        }
        if(id == 1)Main.path.down(now());
    }
}
