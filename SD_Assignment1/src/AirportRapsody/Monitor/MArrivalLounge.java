package AirportRapsody.Monitor;

import AirportRapsody.Interface.IArrivalLoungePassenger;
import AirportRapsody.Interface.IArrivalLoungePorter;
import AirportRapsody.State.SPassenger;
import AirportRapsody.State.SPorter;
import AirportRapsody.Thread.TPassenger;
import AirportRapsody.Thread.TPorter;

import javax.sound.sampled.Port;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MArrivalLounge implements IArrivalLoungePassenger, IArrivalLoungePorter {
    private Integer NUMBER_OF_PASSENGERS;
    private Integer PLANE_PASSENGERS;

    private List<Bag> plane_hold;

    ReentrantLock lock = new ReentrantLock();
    Condition lastPassenger = lock.newCondition();

    public MArrivalLounge(int PLANE_PASSENGERS)
    {
        NUMBER_OF_PASSENGERS = 0;
        plane_hold = new LinkedList<>();
    }

    // PASSENGER
    public void addPassenger(TPassenger Passenger)
    {
        lock.lock();
        NUMBER_OF_PASSENGERS++;
        for(Integer id:Passenger.getBags()) {
            plane_hold.add(new Bag(id, Passenger.getTRANSIT()));
        }
        lock.unlock();
        if(NUMBER_OF_PASSENGERS == PLANE_PASSENGERS){
            lastPassenger.signalAll();
            resetPassenger();
        }
    }

    // PASSENGER
    private void resetPassenger()
    {
        lock.lock();
        NUMBER_OF_PASSENGERS=0;
        lock.unlock();
    }

    @Override
    public void tryToCollectABag() {
        TPorter Porter = (TPorter) Thread.currentThread();
        lock.lock();
        try {
            Porter.setBag(plane_hold.remove(0));
        }catch (Exception e){
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void takeARest() {
        TPorter Porter = (TPorter) Thread.currentThread();
        while(NUMBER_OF_PASSENGERS != PLANE_PASSENGERS) {
            try {
                lastPassenger.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Porter.setCurState(SPorter.AT_THE_PLANES_HOLD);
    }

    @Override
    public void noMoreBagsToCollect() {
        TPorter Porter = (TPorter) Thread.currentThread();
        Porter.setCurState(SPorter.WAITING_FOR_A_PLANE_TO_LAND);
    }

    @Override
    public void carryItToAppropriateStore(Bag bag) {
        TPorter Porter = (TPorter) Thread.currentThread();
        //sleep
        if(bag.getTRANSIT()){
            Porter.setCurState(SPorter.AT_THE_LUGGAGE_BELT_CONVEYOR);
        }
        else{
            Porter.setCurState(SPorter.AT_THE_STOREROOM);
        }
    }

    @Override
    public void whatShouldIDo() {
        TPassenger Passenger = (TPassenger) Thread.currentThread();
        addPassenger(Passenger);
        //meter um sleep random
        if(Passenger.getTRANSIT()){
            takeABus(Passenger);
        }
        else{
            if(Passenger.getBags().size() > 0){
                goCollectABag(Passenger);
            }
            else{
                goHome(Passenger);
            }
        }
    }

    private void takeABus(TPassenger Passenger) {
        Passenger.setCurState(SPassenger.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
    }

    private void goCollectABag(TPassenger Passenger) {
        Passenger.setCurState(SPassenger.AT_THE_LUGGAGE_COLLECTION_POINT);
    }

    private void goHome(TPassenger Passenger) {
        Passenger.setCurState(SPassenger.EXITING_THE_ARRIVAL_TERMINAL);
    }

}
