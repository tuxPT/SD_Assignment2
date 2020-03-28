package AirportRapsody.Interface;

import AirportRapsody.Monitor.Bag;
import AirportRapsody.State.SPorter;

public interface IArrivalLoungePorter {
    Bag tryToCollectABag();
    SPorter takeARest();
    SPorter noMoreBagsToCollect();
    SPorter carryItToAppropriateStore(Bag bag);
}
