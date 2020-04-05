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
    public MArrivalLounge(int PLANE_PASSENGERS, MGeneralRepository MGeneralRepository)
    {
        NUMBER_OF_PASSENGERS = 0;
        this.PLANE_PASSENGERS = PLANE_PASSENGERS;
        plane_hold = new LinkedList<>();
        this.MGeneralRepository = MGeneralRepository;
        this.noMoreWork = false;
        this.noStart = false;
    }



    /**
     * Called by the porter when it reaches the plane hold.<br/>
     * In the case the plane hold is not empty, it'll return the bag in index zero.<br/>
     * @return the bag that was collected or null if the plane hold is empty.
     * @see Bag
     */
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

    /**
     * Called by the porter while is waiting for a plane to land.<br/>
     * Checks if all passengers have disembarked from the plane.<br/>
     * If they did, returns the AT_THE_PLANES_HOLD state, otherwise it'll return the present state (WAITING_FOR_A_PLANE_TO_LAND).<br/>
     * @return Porter's state AT_THE_PLANES_HOLD or WAITING_FOR_A_PLANE_TO_LAND
     * @see SPorter
     */
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
            //NUMBER_OF_PASSENGERS = 0;
            lock.unlock();
        }
        return false;
    }    

    /**
     * Called by the porter after it picks a bag.     <br/>
     * It chooses the next state depending on the bag's type.<br/>
     * If the bag belongs to a passenger having the current airport as the final destination it'll return the conveyor's belt state, 
     * if it's from a passenger that is in transit, it'll return the storeroom's state.
     * @param bag the current bag that the porter is carrying.
     * @return Porter's state AT_THE_STOREROOM or AT_THE_LUGGAGE_BELT_CONVEYOR
     * @see SPorter
     */
    @Override
    public SPorter carryItToAppropriateStore(Bag bag) {       
        //sleep
        assert bag != null : "Bag não existe";
        if(bag.getTRANSIT()){
            return SPorter.AT_THE_STOREROOM;
        }
        else{
            return SPorter.AT_THE_LUGGAGE_BELT_CONVEYOR;
        }
    }

    /**
     * Called by the passengers in the beggining of their cycle (AT_THE_DISEMBARKING_ZONE).<br/>
     * Simulates their arrival to the Arrival Lounge.<br/>
     * Passengers will wait here for every passenger to get to the arrival lounge.<br/>
     * Last passenger to arrive warns the rest and they all get out.<br/>
     * Function will return one of the following passenger's states:<br/>
     * AT_THE_ARRIVAL_TRANSFER_TERMINAL, if the passenger is in transit;<br/>
     * AT_THE_LUGGAGE_COLLECTION_POINT, if the passenger is in the final destination and he has bags to collect;<br/>
     * EXITING_THE_ARRIVAL_TERMINAL, if the passenger is in the final destination but he has no bags to collect<br/>
     * @param id passenger's id (1 up to 6)
     * @param t_bags number of bags the passenger had at the beggining of the journey
     * @param t_TRANSIT true if the passenger is in transit, false otherwise
     * @return Passenger's state AT_THE_ARRIVAL_TRANSFER_TERMINAL, AT_THE_LUGGAGE_COLLECTION_POINT or EXITING_THE_ARRIVAL_TERMINAL
     * @see SPassenger
     */
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

    /**
     * Called by the porter when he goes to the plane's hold and there are none left.<br/>
     * Resets the number of passengers.
     * @return Porter's state WAITING_FOR_A_PLANE_TO_LAND
     * @see SPorter
     */
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
     * Called by the porter.<br/>
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
