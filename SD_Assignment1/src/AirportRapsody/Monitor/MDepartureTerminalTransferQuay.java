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
        MGeneralRepository.updateBusDriver(SBusDriver.PARKING_AT_THE_DEPARTURE_TERMINAL);
        try{
            this.NUMBER_OF_PASSENGERS = NUMBER_OF_PASSENGERS;
            parkTheBus.signalAll();
            lastPassenger.await();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            reset();
            lock.unlock();
        }
        MGeneralRepository.updateBusDriver(SBusDriver.DRIVING_BACKWARD);
        return SBusDriver.DRIVING_BACKWARD;
    }

    @Override
    public SBusDriver goToArrivalTerminal() {
        // SLEEP
        try{
            TimeUnit.MILLISECONDS.sleep(100);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        MGeneralRepository.updateBusDriver(SBusDriver.PARKING_AT_THE_ARRIVAL_TERMINAL);
        return SBusDriver.PARKING_AT_THE_ARRIVAL_TERMINAL;
    }

    @Override
    public SPassenger leaveTheBus(Integer id) {
        lock.lock();
        MGeneralRepository.updatePassenger(SPassenger.TERMINAL_TRANSFER, id, null, null, null, false, null);
        try {
            parkTheBus.await();
            DISEMBARKED_PASSENGERS++;
            if(DISEMBARKED_PASSENGERS == NUMBER_OF_PASSENGERS){
                lastPassenger.signalAll();
            }
            MGeneralRepository.updatePassenger(SPassenger.AT_THE_DEPARTURE_TRANSFER_TERMINAL, id, null, false, null,  false, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();

        }
        return SPassenger.AT_THE_DEPARTURE_TRANSFER_TERMINAL;

    }
}
