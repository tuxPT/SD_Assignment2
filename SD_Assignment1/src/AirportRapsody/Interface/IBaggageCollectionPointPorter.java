package AirportRapsody.Interface;

import AirportRapsody.Monitor.Bag;
import AirportRapsody.State.SPorter;

public interface IBaggageCollectionPointPorter {
    void warnPassengers();
    SPorter addBag(Bag bag);
}
