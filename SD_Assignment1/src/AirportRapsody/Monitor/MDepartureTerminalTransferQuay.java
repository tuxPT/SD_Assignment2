package AirportRapsody.Monitor;

import AirportRapsody.Interface.IDepartureTerminalTransferQuayBusDriver;
import AirportRapsody.Interface.IDepartureTerminalTransferQuayPassenger;
import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.State.SBusDriver;
import AirportRapsody.State.SPassenger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MDepartureTerminalTransferQuay implements IDepartureTerminalTransferQuayBusDriver, IDepartureTerminalTransferQuayPassenger {
    private IGeneralRepository MGeneralRepository;
    Integer NUMBER_OF_PASSENGERS;
    ReentrantLock lock = new ReentrantLock(true);
    Condition parkTheBus = lock.newCondition();
    Condition lastPassenger = lock.newCondition();

    Integer DISEMBARKED_PASSENGERS;

    public MDepartureTerminalTransferQuay(MGeneralRepository MGeneralRepository) {
        DISEMBARKED_PASSENGERS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }
    
    public Integer increment() {
        return DISEMBARKED_PASSENGERS++;
    }

    public void reset() {
        DISEMBARKED_PASSENGERS = 0;
    }

    @Override
    public SBusDriver parkTheBusAndLetPassOff(Integer NUMBER_OF_PASSENGERS) {
        
        lock.lock();
        try{
            this.NUMBER_OF_PASSENGERS = NUMBER_OF_PASSENGERS;
            parkTheBus.signalAll();              
            lastPassenger.await();    
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }            
       
        return SBusDriver.DRIVING_BACKWARD;
    }

    @Override
    public SBusDriver goToArrivalTerminal() {
        // SLEEP
        try{
            TimeUnit.SECONDS.sleep(5);
        }
        catch(Exception e){
            e.printStackTrace();
        }        
        return SBusDriver.PARKING_AT_THE_ARRIVAL_TERMINAL;
    }

    @Override
    public SPassenger leaveTheBus() {
        lock.lock();
        try {
            parkTheBus.await();
            //
            if(DISEMBARKED_PASSENGERS == NUMBER_OF_PASSENGERS){
                lastPassenger.signalAll();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();

        }
        return SPassenger.AT_THE_DEPARTURE_TRANSFER_TERMINAL;

    }
}