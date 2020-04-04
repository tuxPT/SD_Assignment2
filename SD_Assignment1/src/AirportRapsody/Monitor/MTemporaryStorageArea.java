package AirportRapsody.Monitor;

import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.Interface.ITemporaryStorageAreaPorter;
import AirportRapsody.State.SPorter;

public class MTemporaryStorageArea implements ITemporaryStorageAreaPorter {
    private IGeneralRepository MGeneralRepository;
    ReentrantLock lock = new ReentrantLock(true);

    Integer NUMBER_OF_BAGS;

    /**
     * @param MGeneralRepository The General Repository used for logging     
     */
    public MTemporaryStorageArea(MGeneralRepository MGeneralRepository) {
        NUMBER_OF_BAGS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }

    /**
     * Called by the porter.<br/>
     * Adds one more bags to the total of bags currently present in the storeroom.
     @return Porter's state AT_THE_PLANES_HOLD
     @see SPorter
     */
    public SPorter addBag() {
        lock.lock();
        try{
            NUMBER_OF_BAGS++; 
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            MGeneralRepository.updatePorter(SPorter.AT_THE_PLANES_HOLD, null, null, NUMBER_OF_BAGS);
            lock.unlock();
        }
        return SPorter.AT_THE_PLANES_HOLD;
    }
}