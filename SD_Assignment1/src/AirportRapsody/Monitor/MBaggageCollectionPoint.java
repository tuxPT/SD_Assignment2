package AirportRapsody.Monitor;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.IBaggageCollectionPointPorter;

public class MBaggageCollectionPoint implements IBaggageCollectionPointPorter {
    ReentrantLock lock = new ReentrantLock();

    List<Integer> ListOfBags;

    public MBaggageCollectionPoint()
    {
        ListOfBags = new ArrayList<>(); 
    }

    // PORTER
    public void AddBag(int BagID)
    {
        ListOfBags.add(BagID);
    }

     // PASSENGER
     public void RemoveBag(int BagID)
     {
         for (int i = 0; i < ListOfBags.size(); i++)
         {
             if (ListOfBags.get(i) == BagID)
             {
                ListOfBags.remove(i);
             }
         }
     }

    @Override
    public void carryItToAppropriateStore() {

    }

    @Override
    public void tryToCollectABag() {

    }
}
