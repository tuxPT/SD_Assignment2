package AirportRapsody.Monitor;

import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.Interface.ITemporaryStorageAreaPorter;
import AirportRapsody.State.SPorter;

public class MTemporaryStorageArea implements ITemporaryStorageAreaPorter {
    private IGeneralRepository MGeneralRepository;
    ReentrantLock lock = new ReentrantLock(true);

    Integer NUMBER_OF_BAGS;

    public MTemporaryStorageArea(MGeneralRepository MGeneralRepository) {
        NUMBER_OF_BAGS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }

    // only porter can add
    public SPorter addBag() {
        lock.lock();
        try{
            NUMBER_OF_BAGS++; 
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return SPorter.AT_THE_PLANES_HOLD;      
    }
}