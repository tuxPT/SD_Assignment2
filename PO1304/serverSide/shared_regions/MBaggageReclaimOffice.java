package serverSide.shared_regions;

import java.util.concurrent.locks.ReentrantLock;

import common_infrastructures.SPassenger;
import shared_regions_JavaInterfaces.IBaggageReclaimOfficePassenger;
import shared_regions_JavaInterfaces.IGeneralRepository;

public class MBaggageReclaimOffice implements IBaggageReclaimOfficePassenger
{
    private IGeneralRepository MGeneralRepository;
    ReentrantLock lock = new ReentrantLock(true);

    Integer NUMBER_OF_LOST_BAGS;        

    /**
     * @param MGeneralRepository The General Repository used for logging     
     */
    public MBaggageReclaimOffice(IGeneralRepository MGeneralRepository) {
        NUMBER_OF_LOST_BAGS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }
    

    public SPassenger addBag(Integer id, int number_of_bags) {
        lock.lock();
        MGeneralRepository.updatePassenger(SPassenger.AT_THE_BAGGAGE_RECLAIM_OFFICE, id, null, null, null, false, null);
        try{
            NUMBER_OF_LOST_BAGS += number_of_bags;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            lock.unlock();           
        }
        return goHome(id);

    }


    public SPassenger goHome(Integer id)
    {
        return SPassenger.EXITING_THE_ARRIVAL_TERMINAL;
    }
}
