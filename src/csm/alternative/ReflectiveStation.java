package csm.alternative;

import csm.Client.Client;
import csm.Constants;
import csm.observer.StationObserver;
import smart.theatre.distributions.Distribution;

public class ReflectiveStation extends Station{

    private double startService = 0;

    @Msgsrv
    public void init(Distribution d, Integer numServers, Station[] stations, StationObserver observer){
        setDistribution(d);
        setServersNumber(numServers);
        this.stations = stations;
        setObserver(observer);
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        c.setGlobalEnd(now());
        observer().notifyBusyTime(c.getGlobalEnd()-c.getGlobalStart());
        decrementInService();
        if(inService() == 0 || Constants.endTime-now()<10) {
            observer().notifyServiceTime(now() - startService);
            System.out.println("make test");
        }
        send(probabilityDistribution().nextSample(), "departure",c);
    }

    @Override @Msgsrv
    public void departure(Client c) {
        c.setGlobalStart(now());
        if(inService() == 0)
            startService = now();
        incrementInService();
        stations[0].send("arrival",c);
    }
}
