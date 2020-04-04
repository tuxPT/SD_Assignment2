package AirportRapsody.Monitor;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.IBaggageCollectionPointPassenger;
import AirportRapsody.Interface.IBaggageCollectionPointPorter;
import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.State.SPorter;

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

    /**
     * Called by a passenger.<br/>
     * Simulates a passenger waiting in the conveyor's belt for its bags.<br/>
     * Each time the porter puts a bag in the belt, he'll check if it's his.<br/>
     * If it is, it'll remove it from the conveyor's belt.<br/>
     * The passengers will stay here until they have all of their bags or until the porter warns them that there are no more bags left
     * (this means the passenger lost some of its bags).
     @param id passenger's id
     @param total_bags the list of the bag's ID that the passenger had at the beggining of the journey
     @return The number of bags that the passenger collected
     */ 
    public Integer goCollectABag(Integer id, List<Integer> total_bags)
    {          
    lock.lock();
    Integer NUMBER_OF_BAGS_RETRIEVED = 0;
        try{
            while(moreBags) {
                porterArrival.await();
            List<Bag> removeList = new LinkedList<>();
            for (Bag bag: ListOfBags){
                for (Integer i: total_bags){
                    if (i == bag.getID()){
                        removeList.add(bag);
                        NUMBER_OF_BAGS_RETRIEVED++;
                        MGeneralRepository.updatePassenger(null, id, null, null, null, true, null);
                    }
                }   
            }
            ListOfBags.removeAll(removeList);     
            MGeneralRepository.updatePorter(null, null, ListOfBags.size(), null);           
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

    /**
     * Called by the porter.<br/>
     * Porter will add a bag to the conveyor's belt and warn the passengers so they can check if the bag belongs to them.
     * @param bag bag to be added to the conveyor's belt
     * @return Porter's state AT_THE_PLANES_HOLD
     * @see SPorter
     */
    @Override
    public SPorter addBag(Bag bag) {
        lock.lock();
        try{
            ListOfBags.add(bag);
            porterArrival.signalAll();   
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{           
            MGeneralRepository.updatePorter(SPorter.AT_THE_PLANES_HOLD, null, ListOfBags.size(), null);
            lock.unlock();
        }
        return SPorter.AT_THE_PLANES_HOLD;
    }

    /**
     * Called by the porter.<br/>
     * Porter warns the passengers that there are no more bags left.
     */
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
