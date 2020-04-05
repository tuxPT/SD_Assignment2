package AirportRapsody.Monitor;

import AirportRapsody.Interface.IArrivalTerminalTransferQuayBusDriver;
import AirportRapsody.Interface.IArrivalTerminalTransferQuayPassenger;
import AirportRapsody.State.SBusDriver;
import AirportRapsody.State.SPassenger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MArrivalTerminalTransferQuay implements IArrivalTerminalTransferQuayBusDriver, IArrivalTerminalTransferQuayPassenger {
    private MGeneralRepository MGeneralRepository;
    Queue<Integer> WAITING_QUEUE;
    Queue<Integer> BUS_QUEUE;
    Integer BUS_CAPACITY;
    Integer busQueueSize;
    boolean noMoreWork;
    boolean noStart;

    ReentrantLock lock = new ReentrantLock(true);
    Condition waitingQueue = lock.newCondition();
    Condition boarding = lock.newCondition();
    Condition busFull = lock.newCondition();   

    /**
     * @param BUS_CAPACITY max number of passengers the bus can transport in a single trip
     * @param MGeneralRepository The General Repository used for logging   
     */
    public MArrivalTerminalTransferQuay(Integer BUS_CAPACITY, MGeneralRepository MGeneralRepository) {
        WAITING_QUEUE = new LinkedList<Integer>();
        BUS_QUEUE = new LinkedList<Integer>();
        this.BUS_CAPACITY = BUS_CAPACITY;
        this.MGeneralRepository = MGeneralRepository;
        this.noMoreWork = false;
        this.noStart = false;
    }
  
    /**
     * Called by the bus driver.<br/>
     * The bus driver order the passengers to board the bus when there are enough passengers in queue 
     * to fill the whole bus or after a certain time (each few milliseconds, if there is at least
     * one passenger in the waiting queue) to simulate a schedule.<br/>
     * After the passengers starts embarking, the bus driver will wait until the last one to embark
     * warns them that all passengers are aboard.
     * @return BusDriver's state DRIVING_FORWARD
     * @see SBusDriver
     */
    public boolean announcingBusBoarding() {
        //
        lock.lock();
        try{
            MGeneralRepository.updateBusDriver(SBusDriver.PARKING_AT_THE_ARRIVAL_TERMINAL, noStart);
            do{
                waitingQueue.await(1, TimeUnit.MILLISECONDS);
                if(noMoreWork){
                    return true;
                }
                /*if(WAITING_QUEUE.size() == 0){
                    return SBusDriver.PARKING_AT_THE_ARRIVAL_TERMINAL;
                }*/
                //WAITING_QUEUE se WAITING_QUEUE menor ou igual a BUS_CAPACITY senão BUS_CAPACITY
                busQueueSize  = WAITING_QUEUE.size() <= BUS_CAPACITY ? WAITING_QUEUE.size(): BUS_CAPACITY;                
            }
            while(busQueueSize == 0);
            //inicia o boarding
            for(int i=0; i<busQueueSize; i++) {
                boarding.signal();
            }
            //espera que entrem os passageiros sinalizados            
            busFull.await();               
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            busQueueSize = 0;
            lock.unlock();
        }
        return false;
    }    

    /**
     * Called by a passenger.<br/>
     * Passenger will enter the waiting queue.<br/>
     * If its place in the waiting queue is equal to the bus capacity (it means there are enough passengers to fill the bus) he'll warn the busdriver.
     * Otherwise he'll wait until the bus driver warns him to enter the bus.<br/>
     * After receiving order to enter the bus, if he is the last passenger to enter he'll warn the bus driver that the bus is full so he can start the journey.
     * @param passengerID passenger's ID
     * @return Passenger's state TERMINAL_TRANSFER
     * @see SPassenger
     */
    public SPassenger enterTheBus(Integer passengerID) {
        lock.lock();
        try{
            if(!noStart){
                noStart = true;
            }
            WAITING_QUEUE.add(passengerID);
            MGeneralRepository.updatePassenger(SPassenger.AT_THE_ARRIVAL_TRANSFER_TERMINAL, passengerID, true, null, null, false, null);
           if (WAITING_QUEUE.size() == 3)
           {
                waitingQueue.signalAll();
           }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally
        {
            SPassenger tmp = waitToEnterToBus(passengerID);
            lock.unlock();
            return tmp;
        }

    }

    private SPassenger waitToEnterToBus(Integer id){
        try {
            boarding.await();
            Integer passengerID = WAITING_QUEUE.remove();
            assert passengerID == id : "";
            BUS_QUEUE.add(passengerID);           
            MGeneralRepository.updatePassenger(null, passengerID, false, true, null, false, null);
            if (BUS_QUEUE.size() == busQueueSize) {
                busFull.signalAll();
            }
            else{
                busFull.await();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            return SPassenger.TERMINAL_TRANSFER;
        }
    }
       

    /**
     * Called by the driver.<br/>
     * Simulates the bus journey.<br/>
     * Will wait a bit of time (few milliseconds) and return.
     * @return the number of passengers that embarked the bus
     */
    public Integer goToDepartureTerminal() {
        lock.lock();
        MGeneralRepository.updateBusDriver(SBusDriver.DRIVING_FORWARD, true);
        try{
            TimeUnit.MILLISECONDS.sleep(30);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            Integer tmp = BUS_QUEUE.size();
            BUS_QUEUE.clear();
            lock.unlock();
            return tmp;
        }

    }
    public void endOfWork(){
        lock.lock();
        try {
            noMoreWork = true;
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