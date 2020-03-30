package AirportRapsody.Monitor;

import AirportRapsody.Interface.IArrivalLoungePassenger;
import AirportRapsody.Interface.IArrivalLoungePorter;
import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.State.SPassenger;
import AirportRapsody.State.SPorter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
        this.PLANE_PASSENGERS = PLANE_PASSENGERS;
        plane_hold = new LinkedList<>();
        this.MGeneralRepository = MGeneralRepository;
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
                MGeneralRepository.updatePorter(null, plane_hold.size(), null, null);
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
        lock.lock();
        //MGeneralRepository.updatePorter(SPorter.WAITING_FOR_A_PLANE_TO_LAND, plane_hold.size(), null, null);
        try {
                System.out.println("BBBBBBBBBBBBBBBBBB");
                System.out.println(NUMBER_OF_PASSENGERS);
                System.out.println(PLANE_PASSENGERS);
                //while(NUMBER_OF_PASSENGERS < PLANE_PASSENGERS) {
                lastPassenger.await(1, TimeUnit.SECONDS);
                if(NUMBER_OF_PASSENGERS < PLANE_PASSENGERS){
                    return SPorter.WAITING_FOR_A_PLANE_TO_LAND;
                }
                System.out.println("AAAAAAAAAAAAAAAA");
                //}

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            NUMBER_OF_PASSENGERS = 0;
            MGeneralRepository.updatePorter(SPorter.AT_THE_PLANES_HOLD, null, null, null);
            lock.unlock();
        }
        return SPorter.AT_THE_PLANES_HOLD;
    }    

    @Override
    public SPorter carryItToAppropriateStore(Bag bag) {       
        //sleep
        assert bag != null : "Bag não existe";
        if(bag.getTRANSIT()){
            MGeneralRepository.updatePorter(SPorter.AT_THE_STOREROOM, null, null, null);
            return SPorter.AT_THE_STOREROOM;
        }
        else{
            MGeneralRepository.updatePorter(SPorter.AT_THE_LUGGAGE_BELT_CONVEYOR, null, null, null);
            return SPorter.AT_THE_LUGGAGE_BELT_CONVEYOR;
        }
    }

    @Override
    public SPassenger whatShouldIDo(Integer id, Integer t_bags, boolean t_TRANSIT) {         
        lock.lock();
        try{            
            NUMBER_OF_PASSENGERS++;
            System.out.println(PLANE_PASSENGERS);
            System.out.println(NUMBER_OF_PASSENGERS);
            if(NUMBER_OF_PASSENGERS == PLANE_PASSENGERS){
                lastPassenger.signalAll();
            }
            else{
                lastPassenger.await();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
        lock.unlock();
        }          
        MGeneralRepository.updatePassenger(SPassenger.AT_THE_DISEMBARKING_ZONE,
                id,
                null,
                null,
                t_bags != null ? t_bags : 0,
                false,
                t_TRANSIT);
                        //meter um sleep random
        
        if(t_TRANSIT){
            return takeABus(id);
        }
        else{
            if(t_bags > 0){
                return goCollectABag(id);
            }
            else{
                return goHome(id);
            }
        }
    }

    public SPorter noMoreBagsToCollect() {
        MGeneralRepository.updatePorter(SPorter.WAITING_FOR_A_PLANE_TO_LAND, plane_hold.size(), null, null);
        return SPorter.WAITING_FOR_A_PLANE_TO_LAND;
    }

    private SPassenger takeABus(Integer id) {       
        return SPassenger.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
    }

    private SPassenger goCollectABag(Integer id) {
        MGeneralRepository.updatePassenger(SPassenger.AT_THE_LUGGAGE_COLLECTION_POINT, id, null, null, null, false, null);
        return SPassenger.AT_THE_LUGGAGE_COLLECTION_POINT;
    }

    private SPassenger goHome(Integer id) {
        MGeneralRepository.updatePassenger(SPassenger.EXITING_THE_ARRIVAL_TERMINAL, id, null, null, null, false, null);
        return SPassenger.EXITING_THE_ARRIVAL_TERMINAL;
    }

    public void addBag(Bag bag){
        System.out.println(bag.getID());
        plane_hold.add(bag);
    }

}
