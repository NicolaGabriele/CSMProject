package csm.stations;

import csm.Client.Client;
import csm.Constants;
import csm.observer.SimpleObserver;
import smart.theatre.distributions.Distribution;

public class Router extends Station{

    private Station[] stations;
    private SimpleObserver observer;

    @Override
    @Msgsrv
    public void init(Distribution d, Object... vals){
        setDistributionFunction(d );
        Station[] stats = new Station[vals.length-1];
        for(int i = 0; i<vals.length-1; i++)
            if(!(vals[i] instanceof Station))
                throw new IllegalArgumentException("every vals element must be Station instances");
            else
                stats[i] = (Station) vals[i];
        if(!(vals[vals.length-1] instanceof SimpleObserver))
            throw new IllegalArgumentException(String.format("vals[%d] must be an instance of Observer",vals.length-1));
        observer = (SimpleObserver)vals[vals.length-1];
        this.stations = stats;
    }

    @Msgsrv
    @Override
    public void accept(Client client) {
        //client.setStartWaitingTime(now());
        System.out.println(String.format("routeis routing client %d at time %f", client.getId(),now()));
        double serviceTime = (super.getProbabilityDistributionFunction().nextSample()*(Constants.comm_sup-Constants.comm_inf))+Constants.comm_inf;
        send(serviceTime, "finish",client);
        observer.notifyServiceTime(serviceTime);
    }

    @Msgsrv
    @Override
    public void finish(Client client) {
        double guess = Math.random();
        int station = -1;
        double  intervallo1 = Constants.q[0],
                intervallo2 = Constants.q[1] + intervallo1,
                intervallo3 = Constants.q[2] + intervallo2;
        if(guess <= intervallo1)
            station = 0;
        if(guess > intervallo1 && guess <= intervallo2)
            station = 1;
        if(guess > intervallo2 && guess <= intervallo3)
            station = 2;
        if(guess > intervallo3)
            station = 3;
        System.out.println(String.format("client %d routed to station %d at time %f",client.getId(), station,now()));
        stations[station].send(0,"accept",client);
        //observer.notifyBusyTime(now()-client.getStartTime());
    }
}
