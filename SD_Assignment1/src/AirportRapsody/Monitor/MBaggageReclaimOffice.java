package AirportRapsody.Monitor;

import java.util.concurrent.locks.ReentrantLock;

public class MBaggageReclaimOffice {
    ReentrantLock lock = new ReentrantLock();


    Integer NUMBER_OF_LOST_BAGS;

        

    public MBaggageReclaimOffice() {
        NUMBER_OF_LOST_BAGS = 0;
    }
    
    // only passenger can add
    public void addBag() {
        NUMBER_OF_LOST_BAGS++;
    }
}