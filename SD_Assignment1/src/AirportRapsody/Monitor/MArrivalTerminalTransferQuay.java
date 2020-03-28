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
        this.BUS_CAPACITY = BUS_CAPACITY;
        this.MGeneralRepository = MGeneralRepository;
    }
  
    public SBusDriver announcingBusBoarding() {
        lock.lock();
        try{
            do{
                waitingQueue.await(20, TimeUnit.SECONDS);
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
            lock.unlock();            
        }
        return SBusDriver.DRIVING_FORWARD;
    }    

    public SPassenger enterTheBus(Integer passengerID) {
        lock.lock();
        busQueueSize = 0;
        try{
            WAITING_QUEUE.add(passengerID);
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
            lock.unlock();
        }            
        return waitToEnterToBus(passengerID);
    }   

    public SPassenger waitToEnterToBus(Integer passengerID){
        lock.lock();
        try{
            boarding.await();
            WAITING_QUEUE.remove();
            BUS_QUEUE.add(passengerID);

            if(BUS_QUEUE.size() == busQueueSize){
                busFull.signalAll();
            }            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return SPassenger.TERMINAL_TRANSFER;
    }      
       
        
       

    public Integer goToDepartureTerminal() {
        //sleep
        try{
            TimeUnit.SECONDS.sleep(5);
        }
        catch(Exception e){
            e.printStackTrace();
        }        
        return BUS_QUEUE.size();
    }
}