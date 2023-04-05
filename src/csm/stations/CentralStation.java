package csm.stations;

import csm.Client.Client;
import csm.Main;
import csm.observer.SimpleObserver;
import smart.theatre.distributions.Distribution;

public class CentralStation extends Station{


    private Station router;

    private SimpleObserver observer;

    private double startingService;
    private int clientInService;

    //fare modifica service time

    @Override
    @Msgsrv
    public void init( Distribution d , Object...vals) {
        setDistributionFunction(d);
        if( (! (vals[0] instanceof Router)) || (!(vals[1] instanceof SimpleObserver)) )
            throw new IllegalArgumentException("vals[0] must be an Router instance and vals[1] must be an SimpleObserver instance");
        this.router = (Router)vals[0];
        this.observer = (SimpleObserver) vals[1];
        this.clientInService = 0;
        this.startingService = 0;
    }

    @Override
    @Msgsrv
    public void accept(Client client) {
        observer.incrementArrival();
        //client.setStartTime(now());
        if(clientInService == 0){
            clientInService++;
            startingService = now();
        }
        Main.path.up(now());
        send(getProbabilityDistributionFunction().nextSample(), "finish",client);
    }

    @Override
    @Msgsrv
    public void finish(Client client) {
        System.out.println(String.format("client %d have finished in central station and now going to route at time %f\n", client.getId(),now()));
        clientInService--;
        router.send("accept", client);
        //observer.notifyBusyTime(now()- client.getStartTime());
        if(clientInService == 0) {
            observer.notifyServiceTime(now() - startingService);
        }
    }
}
