package AirportRapsody.Monitor;

import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.ITemporaryStorageAreaPorter;
import AirportRapsody.State.SPorter;

public class MTemporaryStorageArea implements ITemporaryStorageAreaPorter {
    private MGeneralRepository MGeneralRepository;
    ReentrantLock lock = new ReentrantLock(true);

    Integer NUMBER_OF_BAGS;

    public MTemporaryStorageArea(MGeneralRepository MGeneralRepository) {
        NUMBER_OF_BAGS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }

    // only porter can add
    public SPorter addBag() {
        NUMBER_OF_BAGS++; 
        return SPorter.AT_THE_PLANES_HOLD;      
    }
}