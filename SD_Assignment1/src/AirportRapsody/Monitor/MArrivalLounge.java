package AirportRapsody.Monitor;

import AirportRapsody.Interface.IArrivalLoungePassenger;

import java.util.concurrent.locks.ReentrantLock;

public class MArrivalLounge implements IArrivalLoungePassenger {
    private Integer NUMBER_OF_PASSENGERS;

    ReentrantLock lock = new ReentrantLock();

    public MArrivalLounge()
    {
        NUMBER_OF_PASSENGERS = 0;
    }

    // PASSENGER
    private void addPassenger()
    {
        NUMBER_OF_PASSENGERS++;
    }

    // PASSENGER
    private void RemovePassenger()
    {
        NUMBER_OF_PASSENGERS--;
    }

    @Override
    public void whatShouldIDo() {

    }

}