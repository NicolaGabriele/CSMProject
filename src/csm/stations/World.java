package csm.stations;

import csm.Client.Client;
import csm.observer.SimpleObserver;
import smart.theatre.distributions.Distribution;

public class World extends Station{

    private Station centralStation;
    private SimpleObserver observer;
    @Override
    @Msgsrv
    public void init(Distribution d,Object... vals) {
        setDistributionFunction(d);
        if( (!(vals[0] instanceof Station)) || (!(vals[1] instanceof SimpleObserver)))
            throw new IllegalArgumentException("central station must be a Station instance and vals[1] must be an instance of Observer");
        this.centralStation = (Station) vals[0];
        this.observer = (SimpleObserver) vals[1];
    }

    @Override
    @Msgsrv
    public void accept(Client client) {
        //client.setStartTime(now());
        System.out.println(String.format("client %d is get in world at time %f", client.getId(),now()));
        double serviceTime = getProbabilityDistributionFunction().nextSample();
        send(serviceTime,"finish",client);
        observer.notifyServiceTime(serviceTime);
    }

    @Override
    @Msgsrv
    public void finish(Client client) {
        System.out.println(String.format("client %d now going out from world at time %f", client.getId(),now()));
        centralStation.send("accept", client);
       // observer.notifyBusyTime(now()-client.getStartTime());
    }
}
