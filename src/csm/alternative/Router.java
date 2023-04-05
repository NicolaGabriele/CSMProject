package csm.alternative;

import csm.Client.Client;
import csm.Constants;
import csm.observer.StationObserver;
import smart.theatre.distributions.Distribution;

public class Router extends Station{

    @Msgsrv
    public void init(Distribution d, Integer numServers, Station[] stations, StationObserver observer){
        setDistribution(d);
        setServersNumber(numServers);
        this.stations = stations;
        setObserver(observer);
    }

    @Override @Msgsrv
    public void arrival(Client c) {
        send(getTimeDelay(), "departure",c);
    }

    @Override @Msgsrv
    public void departure(Client c) {
        Station guess = guessStation();
        guess.send("arrival",c);
    }

    private Station guessStation(){
        int position=0;
        double extracted = probabilityDistribution().nextSample();
        double intervallo1 = Constants.q[0];
        double intervallo2 = Constants.q[1]+intervallo1;
        double intervallo3 = Constants.q[2]+intervallo2;
        if (extracted<=intervallo1) position=0;
        if (intervallo1 < extracted && extracted <= intervallo2) position=1;
        if (intervallo2 < extracted && extracted <= intervallo3) position=2;
        if (intervallo3 < extracted) position=3;
        return stations[position];
    }

    private double getTimeDelay(){
        return probabilityDistribution().nextSample()*(Constants.comm_sup-Constants.comm_inf)+Constants.comm_inf;
    }
}
