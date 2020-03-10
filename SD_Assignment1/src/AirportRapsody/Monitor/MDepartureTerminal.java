package AirportRapsody.Monitor;

import AirportRapsody.Interface.IDepartureTerminalPassenger;

import java.util.concurrent.locks.ReentrantLock;

public class MDepartureTerminal implements IDepartureTerminalPassenger {
    ReentrantLock lock = new ReentrantLock();

    Integer NUMBER_OF_PASSENGERS;

    public MDepartureTerminal()
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