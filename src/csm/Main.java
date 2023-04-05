package csm;

import csm.Client.Client;
import csm.observer.SimpleObserver;
import csm.alternative.*;
import smart.theatre.distributions.*;
import smart.theatre.standalone.ControlMachine;
import smart.theatre.standalone.Simulation;

import java.util.Random;

public class Main {

    public static Path path = new Path();

    public static void main(String...args){

        Random random = new Random();

        ControlMachine sim = new Simulation(Constants.endTime);

        Distribution ds0 = new ExponentialDistribution(random, Constants.mu0),
                     ds1 = new ExponentialDistribution(random, Constants.mu1),
                     ds2 = new ExponentialDistribution(random, Constants.mu2),
                     ds3 = new HyperExponentialDistribution(random,Constants.a3,Constants.mu3),
                     ds4 = new ErlangDistribution(random,Constants.n4,Constants.mu4),
                     drouter = new UniformDistribution(random,0,1);

        SimpleObserver centerObs = new SimpleObserver(Constants.endTime),
                       worldObs = new SimpleObserver(Constants.endTime),
                       s2Obs = new SimpleObserver(Constants.endTime),
                       s3Obs = new SimpleObserver(Constants.endTime),
                       s4Obs = new SimpleObserver(Constants.endTime),
                       routerObs = new SimpleObserver(Constants.endTime);

        Station world = new ReflectiveStation();
        Station center = new KServerStation();
        Station s2 = new KServerStation(),
                s3 = new KServerStation(),
                s4 = new KServerStation();
        Station router = new Router();

        world.send("init", ds0,Constants.NUM_CLIENTS, new Station[]{center},worldObs);
        center.send("init", ds1, 3, 1,new Station[] {router},centerObs);
        s2.send("init",ds2,1,2,new Station[]{center},s2Obs);
        s3.send("init",ds3,1,3,new Station[]{center},s3Obs);
        s4.send("init",ds4,1,4,new Station[]{center},s4Obs);
        router.send("init", drouter,Constants.NUM_CLIENTS,new Station[]{world,s2,s3,s4},routerObs);


        Client[] clients = new Client[Constants.NUM_CLIENTS];
        for(int j = 0; j<clients.length; j++){
            clients[j] = new Client(j);
            world.send("departure",clients[j]);
        }
        sim.controller();


        System.out.println(String.format("Resume stazione 1:\n Sojourn time: %f\n Throughput: %f\n Utilizzation: %f\n\n",
                centerObs.getSoujornTime(),centerObs.getThroughput(),centerObs.getUtilizzation()));

        System.out.println(String.format("Resume stazione 2:\n Sojourn time: %f\n Throughput: %f\n Utilizzation: %f\n\n",
                s2Obs.getSoujornTime(),s2Obs.getThroughput(),s2Obs.getUtilizzation()));

        System.out.println(String.format("Resume stazione 3:\n Sojourn time: %f\n Throughput: %f\n Utilizzation: %f\n\n",
                s3Obs.getSoujornTime(),s3Obs.getThroughput(),s3Obs.getUtilizzation()));


        System.out.println(String.format("Resume stazione 4:\n Sojourn time: %f\n Throughput: %f\n Utilizzation: %f\n\n",
                s4Obs.getSoujornTime(),s4Obs.getThroughput(),s4Obs.getUtilizzation()));

        System.out.println(String.format(
                "numero di clienti serviti:\n stazione 1: %d\n stazione 2: %d\n stazione 3: %d\n stazione 4:%d\n\n",
                centerObs.getArrivi(), s2Obs.getArrivi(), s3Obs.getArrivi(), s4Obs.getArrivi()
        ));

        System.out.println(
                String.format(
                    "grado di parallelismo: %f\n\n",path.mean(Constants.endTime)
                )
        );

        System.out.println(
                String.format(
                        "Dati per l'intero sistema:\n Sojourn time: %f\n Throughput: %f\n Utilizzation: %f\n\n",
                        worldObs.getSoujornTime(),worldObs.getThroughput(),worldObs.getUtilizzation()
                )
        );
    }
}
