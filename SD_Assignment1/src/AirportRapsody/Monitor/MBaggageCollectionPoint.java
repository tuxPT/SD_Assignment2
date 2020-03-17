package AirportRapsody.Monitor;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.IBaggageCollectionPointPassenger;
import AirportRapsody.Interface.IBaggageCollectionPointPorter;

public class MBaggageCollectionPoint implements IBaggageCollectionPointPorter, IBaggageCollectionPointPassenger {
    ReentrantLock lock = new ReentrantLock();

    List<Bag> ListOfBags;

    public MBaggageCollectionPoint()
    {
        ListOfBags = new ArrayList<>(); 
    }

     // PASSENGER
     public void RemoveBag(int BagID)
     {

     }

    @Override
    public void addBag(Bag bag) {
        lock.lock();
        ListOfBags.add(bag);
        lock.unlock();

    }
}
