package shared_regions;

import java.util.concurrent.locks.ReentrantLock;

import shared_regions.IGeneralRepository;
import shared_regions.ITemporaryStorageAreaPorter;
import common_infrastructures.SPorter;

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


    public SPorter addBag() {
        lock.lock();
        try{
            MGeneralRepository.updatePorter(SPorter.AT_THE_STOREROOM, null, null, NUMBER_OF_BAGS, true);
            NUMBER_OF_BAGS++; 
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return SPorter.AT_THE_PLANES_HOLD;
    }
}