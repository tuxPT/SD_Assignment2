package shared_regions;

import shared_regions.IArrivalTerminalTransferQuayBusDriver;
import shared_regions.IArrivalTerminalTransferQuayPassenger;
import common_infrastructures.SBusDriver;
import common_infrastructures.SPassenger;

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
  

    public boolean announcingBusBoarding() {
        //
        lock.lock();
        try{
            MGeneralRepository.updateBusDriver(SBusDriver.PARKING_AT_THE_ARRIVAL_TERMINAL, noStart);
            do{
                waitingQueue.await(5, TimeUnit.MILLISECONDS);
                if(noMoreWork){
                    return true;
                }
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

    @Override
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