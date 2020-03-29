package AirportRapsody.Monitor;

import AirportRapsody.Interface.IArrivalLoungePassenger;
import AirportRapsody.Interface.IArrivalLoungePorter;
import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.State.SPassenger;
import AirportRapsody.State.SPorter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MArrivalLounge implements IArrivalLoungePassenger, IArrivalLoungePorter {
    private IGeneralRepository MGeneralRepository;
    private Integer NUMBER_OF_PASSENGERS;
    private Integer PLANE_PASSENGERS;

    private List<Bag> plane_hold;

    ReentrantLock lock = new ReentrantLock(true);
    Condition lastPassenger = lock.newCondition();

    public MArrivalLounge(int PLANE_PASSENGERS, MGeneralRepository MGeneralRepository)
    {
        NUMBER_OF_PASSENGERS = 0;
        plane_hold = new LinkedList<>();
        this.MGeneralRepository = MGeneralRepository;
    }

    // PASSENGER
    public void addPassenger()
    {        
        lock.lock();
        try{            
            NUMBER_OF_PASSENGERS++;
            if(NUMBER_OF_PASSENGERS == PLANE_PASSENGERS){
                lastPassenger.signalAll();
                NUMBER_OF_PASSENGERS = 0;   
                }            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
        lock.unlock();
        }
    }

    // PASSENGER
    
    @Override
    public Bag tryToCollectABag() {
        Bag tmp = null;
        lock.lock();
        try {           
            if (plane_hold.size() != 0)
            {
                tmp = plane_hold.remove(0);
            }                    
        }catch (Exception e){
            e.printStackTrace();
        } 
        finally {
            lock.unlock();
        }
        return tmp;
    }

    @Override
    public SPorter takeARest() {
        while(NUMBER_OF_PASSENGERS != PLANE_PASSENGERS) {
            lock.lock();
            try {
                lastPassenger.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                lock.unlock();
            }
        }    
        return SPorter.AT_THE_PLANES_HOLD;        
    }    

    @Override
    public SPorter carryItToAppropriateStore(Bag bag) {       
        //sleep
        if(bag.getTRANSIT()){
            return SPorter.AT_THE_LUGGAGE_BELT_CONVEYOR;
        }
        else{
            return SPorter.AT_THE_STOREROOM;
        }
    }

    @Override
    public SPassenger whatShouldIDo(Integer id, Integer t_bags, boolean t_TRANSIT) {
        addPassenger();
        MGeneralRepository.updatePassenger(SPassenger.AT_THE_DISEMBARKING_ZONE,
                id,
                null,
                null,
                t_bags != null ? t_bags : 0,
                0,
                t_TRANSIT);
                        //meter um sleep random
        if(t_TRANSIT){
            return takeABus();
        }
        else{
            if(t_bags > 0){
                return goCollectABag();
            }
            else{
                return goHome();
            }
        }
    }

    public SPorter noMoreBagsToCollect() { 
        return SPorter.WAITING_FOR_A_PLANE_TO_LAND;    
    }

    private SPassenger takeABus() {
        return SPassenger.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
    }

    private SPassenger goCollectABag() {
        return SPassenger.AT_THE_LUGGAGE_COLLECTION_POINT;
    }

    private SPassenger goHome() {
        return SPassenger.EXITING_THE_ARRIVAL_TERMINAL;
    }

    public void addBag(Bag bag){
        plane_hold.add(bag);
    }
}
