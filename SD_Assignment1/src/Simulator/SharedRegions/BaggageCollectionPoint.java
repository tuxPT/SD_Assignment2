package Simulator.SharedRegions;

import java.util.List;
import java.util.ArrayList;

interface BaggageCollectionPointInterface {
    static void addBag() {};
    static void RemoveBag() {};    
}

public class BaggageCollectionPoint implements BaggageCollectionPointInterface
{
    List<Integer> ListOfBags;

    public BaggageCollectionPoint() 
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
}
