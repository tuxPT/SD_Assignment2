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

    ReentrantLock lock = new ReentrantLock(true);
    Condition waitingQueue = lock.newCondition();
    Condition boarding = lock.newCondition();
    Condition busFull = lock.newCondition();   

    public MArrivalTerminalTransferQuay(Integer BUS_CAPACITY, MGeneralRepository MGeneralRepository) {
        WAITING_QUEUE = new LinkedList<Integer>();
        BUS_QUEUE = new LinkedList<Integer>();
        this.BUS_CAPACITY = BUS_CAPACITY;
        this.MGeneralRepository = MGeneralRepository;
    }
  
    public SBusDriver announcingBusBoarding() {
        //MGeneralRepository.updateBusDriver(SBusDriver.PARKING_AT_THE_ARRIVAL_TERMINAL);
        lock.lock();
        try{
            do{
                waitingQueue.await(10, TimeUnit.SECONDS);
                if(WAITING_QUEUE.size() == 0){
                    return SBusDriver.PARKING_AT_THE_ARRIVAL_TERMINAL;
                }
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
        MGeneralRepository.updateBusDriver(SBusDriver.DRIVING_FORWARD);
        return SBusDriver.DRIVING_FORWARD;
    }    

    public SPassenger enterTheBus(Integer passengerID) {
        lock.lock();
        try{
            WAITING_QUEUE.add(passengerID);
            MGeneralRepository.updatePassenger(SPassenger.AT_THE_ARRIVAL_TRANSFER_TERMINAL, passengerID, true, null, null, false, null);
           if (WAITING_QUEUE.size() == 3)
           {
                waitingQueue.signalAll();
           }
           return waitToEnterToBus(passengerID);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally
        {            
            lock.unlock();
        }
        return waitToEnterToBus(passengerID);
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return SPassenger.TERMINAL_TRANSFER;
    }      
       
        
       

    public Integer goToDepartureTerminal() {
        try{
            TimeUnit.SECONDS.sleep(5);
        }
        catch(Exception e){
            e.printStackTrace();
        }   
        Integer tmp = BUS_QUEUE.size();
        BUS_QUEUE.clear();     
        return tmp;
    }
}