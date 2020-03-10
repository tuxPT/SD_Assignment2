package AirportRapsody.Monitor;

import java.util.concurrent.locks.ReentrantLock;

public class MTemporaryStorageArea {
    ReentrantLock lock = new ReentrantLock();

    Integer NUMBER_OF_BAGS;

    public MTemporaryStorageArea() {
        NUMBER_OF_BAGS = 0;
    }

    // only porter can add
    public void addBag() {
        NUMBER_OF_BAGS++;
    }
}