package serverSide.shared_regions;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import common_infrastructures.Bag;
import common_infrastructures.SPassenger;
import common_infrastructures.SPorter;
import shared_regions_JavaInterfaces.IArrivalLoungePassenger;
import shared_regions_JavaInterfaces.IArrivalLoungePorter;
import shared_regions_JavaInterfaces.IGeneralRepository;

public class MArrivalLounge implements IArrivalLoungePassenger, IArrivalLoungePorter {
    private IGeneralRepository MGeneralRepository;
    private Integer NUMBER_OF_PASSENGERS;
    private Integer PLANE_PASSENGERS;
    private boolean noMoreWork;
    private boolean noStart;
    private List<Bag> plane_hold;

    ReentrantLock lock = new ReentrantLock(true);
    Condition lastPassenger = lock.newCondition();
    Condition waitingPlaneToLand = lock.newCondition();

    /**
     * @param PLANE_PASSENGERS The number of passengers per plane
     * @param MGeneralRepository The General Repository used for logging
     */
    public MArrivalLounge(int PLANE_PASSENGERS, IGeneralRepository MGeneralRepository)
    {
        NUMBER_OF_PASSENGERS = 0;
        this.PLANE_PASSENGERS = PLANE_PASSENGERS;
        plane_hold = new LinkedList<>();
        this.MGeneralRepository = MGeneralRepository;
        this.noMoreWork = false;
        this.noStart = false;
    }

    @Override
    public Bag tryToCollectABag() {
        Bag tmp = null;
        lock.lock();
        try {
            if (plane_hold.size() != 0)
            {
                tmp = plane_hold.remove(0);
                MGeneralRepository.updatePorter(SPorter.AT_THE_PLANES_HOLD, plane_hold.size(), null, null, true);
            }
            else{
                MGeneralRepository.updatePorter(SPorter.AT_THE_PLANES_HOLD, null, null, null, true);
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
    public boolean takeARest() {
        lock.lock();
        try {
            MGeneralRepository.updatePorter(SPorter.WAITING_FOR_A_PLANE_TO_LAND, null, null, null, noStart);
            do {
                waitingPlaneToLand.signal();
                if (noMoreWork) {
                    return true;
                }
                lastPassenger.await(5, TimeUnit.MILLISECONDS);

            }while(NUMBER_OF_PASSENGERS < PLANE_PASSENGERS);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
        return false;
    }    

    @Override
    public SPorter carryItToAppropriateStore(Bag bag) {       
        assert bag != null : "Bag não existe";
        if(bag.getTRANSIT()){
            return SPorter.AT_THE_STOREROOM;
        }
        else{
            return SPorter.AT_THE_LUGGAGE_BELT_CONVEYOR;
        }
    }


    @Override
    public SPassenger whatShouldIDo(Integer id, Integer t_bags, boolean t_TRANSIT) {         
        lock.lock();
        try{
            NUMBER_OF_PASSENGERS++;
            if(!noStart){
                noStart = true;
            }
            MGeneralRepository.updatePassenger(SPassenger.AT_THE_DISEMBARKING_ZONE,
                    id,
                    null,
                    null,
                    t_bags != null ? t_bags : 0,
                    false,
                    t_TRANSIT);
            if(NUMBER_OF_PASSENGERS == PLANE_PASSENGERS){
                lastPassenger.signalAll();
            }
            else{
                //lastPassenger.await();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
        SPassenger tmp;

        //meter um sleep random
        if(t_TRANSIT){
            tmp = takeABus();
        }
        else{
            if(t_bags > 0){
                tmp = goCollectABag();
            }
            else{
                tmp = goHome();
            }
        }
        return tmp;
    }


    public SPorter noMoreBagsToCollect() {
        lock.lock();
        NUMBER_OF_PASSENGERS = 0;
        lock.unlock();
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

    /**
     * Called by the AirportRapsody.main.AirportRapsody.<br>
     * Adds a bag to the plane's hold.
     * @param bag bag that needs to be added to the plane's hold
     */
    public void addBag(Bag bag){
        lock.lock();
        try {
            plane_hold.add(bag);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
    public void endOfWork(){
        lock.lock();
        try{
            noMoreWork = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
    public void waitForPorter(){
        lock.lock();
        try{
            waitingPlaneToLand.await();
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            lock.unlock();
        }
    }
}
