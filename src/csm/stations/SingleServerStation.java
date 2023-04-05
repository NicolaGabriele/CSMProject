package csm.stations;

import csm.Client.Client;
import csm.observer.SimpleObserver;
import smart.theatre.distributions.Distribution;
import java.util.LinkedList;


/**
 * implement a single server policy station
 */
public class SingleServerStation extends Station{

    private boolean FREE = true;
    private LinkedList<Client> waitingLine;
    private Station central;
    private double startingTime;
    private int inService;

    private SimpleObserver observer;
    @Override
    @Msgsrv
    public void init( Distribution d,Object...vals) {
        setDistributionFunction(d);
        if((! (vals[0] instanceof Station)) || (!(vals[1] instanceof SimpleObserver)))
            throw new IllegalArgumentException("vals[0] must be a Station instance and vals[1] must be a Observer instance");
        this.central = (Station)vals[0];
        this.observer = (SimpleObserver)vals[1];
        this.waitingLine = new LinkedList<>();
        this.inService = 0;
        this.startingTime = 0;
    }

    @Override
    @Msgsrv
    public void accept(Client client) {
        observer.incrementArrival();
       // client.setStartTime(now());
        if(! FREE) {
            System.out.println(String.format("client %d in here but station is busy, client must go into waitingLine at time %f", client.getId(),now()));
            waitingLine.addLast(client);
        }
        else {
            FREE = false;
            if(waitingLine.size() == 0)
                startingTime = now();
            System.out.println(String.format("client %d is here and the station is free, starting to serve him at time %f",client.getId(),now()));
            double serviceTime = super.getProbabilityDistributionFunction().nextSample();
            send(serviceTime, "finish", client);
            observer.notifyServiceTime(serviceTime);
        }
    }

    @Override
    @Msgsrv
    public void finish(Client client) {
        System.out.println(String.format("finished to serve client %d, now he can go out at time %f", client.getId(),now()));
        if(waitingLine.size() > 0)
            send(0,"finish",waitingLine.removeFirst());
        else {
            FREE = true;
            observer.notifyBusyTime(now() - startingTime);
        }
        central.send("accept", client);
    }


}//SingleServerStation
