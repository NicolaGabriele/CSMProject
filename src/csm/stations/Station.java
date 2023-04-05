package csm.stations;
import csm.Client.Client;
import smart.theatre.distributions.Distribution;
import smart.theatre.standalone.Actor;

/**
 * this class models a component of CSM (Central Service Model) Actors system that rappresent an abstraction of
 * a service station
 */
public abstract class Station extends Actor{

    private Distribution distribution;


    public abstract void init(Distribution d,Object... vals);


    /**
     * this method implement policy of service of the station, each station type has a different policy
     * and this policy can be defined by implementation of it
     * @param client: is the that must be serve
     */

    public abstract void accept(Client client);

    /**
     * implement the policy of a service termination, each station type must define this policy by implementation
     * of this method
     * @param client: is the client that is ready to go out from the station
     */

    public abstract void finish(Client client);


    /**
     * getter method
     * @return the distribution function that the station use
     */
    public Distribution getProbabilityDistributionFunction(){
        return this.distribution;
    }//getProbabilityDistributionFunction

    /**
     * riserved to subclass
     * @param d: distribution function that class must will use
     */
    protected void setDistributionFunction(Distribution d){
        if(d == null)
            throw new IllegalArgumentException(); // if user pass a null distribution function station cannot work correctly
        this.distribution = d;
    }

}//Station
