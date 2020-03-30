package AirportRapsody.Monitor;

import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.IBaggageReclaimOfficePassenger;
import AirportRapsody.State.SPassenger;
import AirportRapsody.Interface.IGeneralRepository;

public class MBaggageReclaimOffice implements IBaggageReclaimOfficePassenger
{
    private IGeneralRepository MGeneralRepository;
    ReentrantLock lock = new ReentrantLock(true);

    Integer NUMBER_OF_LOST_BAGS;        

    public MBaggageReclaimOffice(MGeneralRepository MGeneralRepository) {
        NUMBER_OF_LOST_BAGS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }
    
    // only passenger can add
    public SPassenger addBag(Integer id, int number_of_bags) {
        lock.lock();
        try{
            // SLEEP
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
        MGeneralRepository.updatePassenger(SPassenger.EXITING_THE_ARRIVAL_TERMINAL, id, null, null, null, false, null);
        return SPassenger.EXITING_THE_ARRIVAL_TERMINAL;
    }
}