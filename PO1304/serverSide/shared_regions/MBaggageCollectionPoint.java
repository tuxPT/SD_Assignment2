package shared_regions;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import shared_regions.IBaggageCollectionPointPassenger;
import shared_regions.IBaggageCollectionPointPorter;
import shared_regions.IGeneralRepository;
import common_infrastructures.SPassenger;
import common_infrastructures.SPorter;
import common_infrastructures.Bag;

public class MBaggageCollectionPoint implements IBaggageCollectionPointPorter, IBaggageCollectionPointPassenger {
    private IGeneralRepository MGeneralRepository;
    ReentrantLock lock = new ReentrantLock(true);
    Condition porterArrival = lock.newCondition();
    private boolean moreBags;      

    List<Bag> ListOfBags;

    /**
     * @param MGeneralRepository The General Repository used for logging     
     */
    public MBaggageCollectionPoint(MGeneralRepository MGeneralRepository)
    {
        ListOfBags = new ArrayList<>(); 
        this.moreBags = true;
        this.MGeneralRepository = MGeneralRepository;
    }


    public Integer goCollectABag(Integer id, List<Integer> total_bags)
    {          
    lock.lock();
    Integer NUMBER_OF_BAGS_RETRIEVED = 0;
        MGeneralRepository.updatePassenger(SPassenger.AT_THE_LUGGAGE_COLLECTION_POINT, id, null, null, null, false, null);
        try{
            while(moreBags) {
                porterArrival.await();
                List<Bag> removeList = new LinkedList<>();
                for (Bag bag: ListOfBags){
                    for (Integer i: total_bags){
                        if (i == bag.getID()){
                            removeList.add(bag);
                            NUMBER_OF_BAGS_RETRIEVED++;
                            MGeneralRepository.updatePorter(null, null, ListOfBags.size(), null, false);
                            MGeneralRepository.updatePassenger(null, id, null, null, null, true, null);
                        }
                    }
                }
                ListOfBags.removeAll(removeList);
                if (total_bags.size() == NUMBER_OF_BAGS_RETRIEVED)
                {
                    return NUMBER_OF_BAGS_RETRIEVED;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
        return NUMBER_OF_BAGS_RETRIEVED;
     }     


    @Override
    public SPorter addBag(Bag bag) {
        lock.lock();
        try{
            ListOfBags.add(bag);
            MGeneralRepository.updatePorter(SPorter.AT_THE_LUGGAGE_BELT_CONVEYOR, null, ListOfBags.size(), null, true);
            porterArrival.signalAll();   
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{           
            lock.unlock();
        }
        return SPorter.AT_THE_PLANES_HOLD;
    }


    public void warnPassengers(){
        lock.lock();
        try{
            this.moreBags = false;
            porterArrival.signalAll();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * Resets the CollectionPoint's state to be ready to receive the next plane.
     */
    public void newPlane(){
        lock.lock();
        try{
            this.moreBags = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}
