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

    /**
     * @param MGeneralRepository The General Repository used for logging     
     */
    public MBaggageReclaimOffice(MGeneralRepository MGeneralRepository) {
        NUMBER_OF_LOST_BAGS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }
    
    /**
     * Called by a passenger.<br/>
     * Adds to the total number of lost bags the number of lost bags that the passenger has lost.
     @param id passenger's id
     @param number_of_bags number of bags that the current passenger lost
     @return Passenger's state EXITING_THE_ARRIVAL_TERMINAL
     @see SPassenger
     */
    public SPassenger addBag(Integer id, int number_of_bags) {
        lock.lock();
        MGeneralRepository.updatePassenger(SPassenger.AT_THE_BAGGAGE_RECLAIM_OFFICE, id, null, null, null, false, null);
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

    /**
     @param id passenger's id
     @return Passenger's state EXITING_THE_ARRIVAL_TERMINAL
     @see SPassenger
     */
    public SPassenger goHome(Integer id)
    {
        return SPassenger.EXITING_THE_ARRIVAL_TERMINAL;
    }
}
