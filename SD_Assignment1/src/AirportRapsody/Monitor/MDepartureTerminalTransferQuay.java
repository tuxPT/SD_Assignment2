package AirportRapsody.Monitor;

import AirportRapsody.Interface.IDepartureTerminalTransferQuayBusDriver;
import AirportRapsody.Interface.IDepartureTerminalTransferQuayPassenger;

import java.util.concurrent.locks.ReentrantLock;

public class MDepartureTerminalTransferQuay implements IDepartureTerminalTransferQuayBusDriver, IDepartureTerminalTransferQuayPassenger {
    ReentrantLock lock = new ReentrantLock();

    Integer DISEMBARKED_PASSENGERS;

    public MDepartureTerminalTransferQuay() {
        DISEMBARKED_PASSENGERS = 0;
    }
    
    public Integer increment() {
        return DISEMBARKED_PASSENGERS++;
    }

    public void reset() {
        DISEMBARKED_PASSENGERS = 0;
    }

    @Override
    public void parkTheBusAndLetPassOff() {

    }

    @Override
    public void goToArrivalTerminal() {

    }

    @Override
    public void leaveTheBus() {

    }
}