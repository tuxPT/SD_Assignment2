package AirportRapsody.Monitor;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.IBaggageCollectionPointPassenger;
import AirportRapsody.Interface.IBaggageCollectionPointPorter;
import AirportRapsody.State.SPorter;

public class MBaggageCollectionPoint implements IBaggageCollectionPointPorter, IBaggageCollectionPointPassenger {
    private MGeneralRepository MGeneralRepository;
    ReentrantLock lock = new ReentrantLock(true);
    Condition porterArrival = lock.newCondition();
    private boolean moreBags;      

    List<Bag> ListOfBags;

    public MBaggageCollectionPoint(MGeneralRepository MGeneralRepository)
    {
        ListOfBags = new ArrayList<>(); 
        this.moreBags = true;
        this.MGeneralRepository = MGeneralRepository;
    }

     // PASSENGER
     public Integer goCollectABag(List<Integer> total_bags)
     {          
        Integer NUMBER_OF_BAGS_RETRIEVED = 0;
        lock.lock();
        try{            
            do{
            porterArrival.await();                    
                for (Bag bag: ListOfBags){
                    for (Integer i: total_bags){
                        // BAG BELONGS TO PASSENGER
                        if (i == bag.getID()){
                            ListOfBags.remove(bag);
                            NUMBER_OF_BAGS_RETRIEVED++;
                        }
                    }   
                }          
                if (total_bags.size() == NUMBER_OF_BAGS_RETRIEVED)
                {
                    return NUMBER_OF_BAGS_RETRIEVED;
                }   
            }   
            while(moreBags);                                         
        }
        catch(Exception e){
        }
        finally{
            lock.unlock();
        }
        return NUMBER_OF_BAGS_RETRIEVED;
     }     

    // PORTER

    @Override
    public SPorter addBag(Bag bag) {
        lock.lock();
        try{
            ListOfBags.add(bag);
            porterArrival.signalAll();           
        }
        catch (Exception e){
        }
        finally{
            lock.unlock();
        } 
        return SPorter.AT_THE_PLANES_HOLD;
    }

    public void warnPassengers(){
        this.moreBags = false;
        porterArrival.signalAll(); 
    }
   
}
