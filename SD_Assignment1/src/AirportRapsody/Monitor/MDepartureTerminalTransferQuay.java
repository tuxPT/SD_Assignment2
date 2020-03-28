package AirportRapsody.Monitor;

import AirportRapsody.Interface.IDepartureTerminalTransferQuayBusDriver;
import AirportRapsody.Interface.IDepartureTerminalTransferQuayPassenger;
import AirportRapsody.State.SBusDriver;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MDepartureTerminalTransferQuay implements IDepartureTerminalTransferQuayBusDriver, IDepartureTerminalTransferQuayPassenger {
    private MGeneralRepository MGeneralRepository;
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
        }
        catch (Exception e){}
        finally {
            lock.unlock();
        }
        parkTheBus.signalAll();
        return SBusDriver.DRIVING_BACKWARD;
    }

    @Override
    public SBusDriver goToArrivalTerminal() {
        lock.lock();
        try{
            lastPassenger.await();
            return SBusDriver.DRIVING_FORWARD;
        }
        catch (Exception e){
            return SBusDriver.PARKING_AT_THE_DEPARTURE_TERMINAL;
        }
        finally {
            lock.unlock();
        }

    }

    @Override
    public void leaveTheBus() {
        lock.lock();
        try {
            parkTheBus.await();
            if(DISEMBARKED_PASSENGERS == NUMBER_OF_PASSENGERS){
                lastPassenger.signalAll();
            }
        }catch (Exception e){}
        finally {
            lock.unlock();
        }
        //retornar estado
    }
}