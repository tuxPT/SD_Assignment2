package AirportRapsody.Interface;

import AirportRapsody.Monitor.Bag;

public interface IArrivalLoungePorter {
    void tryToCollectABag();
    void takeARest();
    void noMoreBagsToCollect();
    void carryItToAppropriateStore(Bag bag);
}
