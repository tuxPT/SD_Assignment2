package AirportRapsody.Monitor;

import AirportRapsody.Interface.IArrivalTerminalExitPassenger;

import java.util.concurrent.locks.ReentrantLock;

public class MArrivalTerminalExit implements IArrivalTerminalExitPassenger {
    Integer NUMBER_OF_PASSENGERS;

    ReentrantLock lock = new ReentrantLock();

    public MArrivalTerminalExit()
    {
        NUMBER_OF_PASSENGERS = 0;
    }

    // PASSENGER
    public void addPassenger()
    {
        NUMBER_OF_PASSENGERS++;
    }

    // PASSENGER
    public void RemovePassenger()
    {
        NUMBER_OF_PASSENGERS--;
    }

    @Override
    public void goHome() {

    }

    @Override
    public void prepareNextLeg() {

    }
}