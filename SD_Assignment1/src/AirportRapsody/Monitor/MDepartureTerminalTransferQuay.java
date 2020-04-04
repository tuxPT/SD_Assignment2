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

    /**
     * @param MGeneralRepository The General Repository used for logging     
     */
    public MDepartureTerminalTransferQuay(MGeneralRepository MGeneralRepository) {
        DISEMBARKED_PASSENGERS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }
    
    /**
     * Increments the number of disembarked by one.
     * @return the number of disembarked passengers.
     */
    public Integer increment() {
        return DISEMBARKED_PASSENGERS++;
    }

    /**
     * Resets the number of passenger's that have disembarked counter to zero.
     */
    public void reset() {
        DISEMBARKED_PASSENGERS = 0;
    }

    /**
     * Called by the bus driver.<br/>
     * Will warn the passengers that the bus has arrived so they can start disembarking.<br/>
     * After being warned by the last passenger that all passengers have disembarked, it returns.
     * @param NUMBER_OF_PASSENGERS number of passengers that were transport by the bus in the current journey.<br/>
     * @return Bus driver's state DRIVING_BACKWARD
     * @see SBusDriver
     */
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

    /**
     * Called by the driver.<br/>
     * Simulates the bus journey.<br/>
     * Will wait a bit of time (few milliseconds) and return.
     * @return Bus driver's PARKING_AT_THE_ARRIVAL_TERMINAL
     * @see SBusDriver
     */
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

    /**
     * Called by a passenger.<br/>
     * Passenger will wait until being warned by the bus driver that the bus has arrived so he can disembark.<br/>
     * If he is the last passenger disembarking he'll warn the bus driver so he can go back to the arrival terminal.
     * @param id passenger's id
     * @return Passenger's state AT_THE_DEPARTURE_TRANSFER_TERMINAL
     * @see SPassenger
     */
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
