package AirportRapsody.Interface;

import AirportRapsody.Monitor.Bag;
import AirportRapsody.State.SPorter;

public interface IArrivalLoungePorter {
    Bag tryToCollectABag();
    boolean takeARest();
    SPorter noMoreBagsToCollect();
    SPorter carryItToAppropriateStore(Bag bag);
}
