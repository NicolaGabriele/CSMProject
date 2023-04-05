package csm.alternative;

import csm.Client.Client;
import csm.Constants;
import csm.observer.StationObserver;
import smart.theatre.distributions.Distribution;

public class ReflectiveStation extends Station{

    private double startService = 0;
    private boolean doneService = false;

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
        if(inService() == 0) {
            observer().notifyServiceTime(now() - startService);
            doneService = true;
        }
        if(Constants.endTime-now()<10 && !doneService)
            observer().setServiceTime(now() - startService);
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
