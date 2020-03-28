package AirportRapsody.Monitor;

import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.IBaggageReclaimOfficePassenger;
import AirportRapsody.State.SPassenger;

public class MBaggageReclaimOffice implements IBaggageReclaimOfficePassenger
{
    private MGeneralRepository MGeneralRepository;
    ReentrantLock lock = new ReentrantLock(true);

    Integer NUMBER_OF_LOST_BAGS;        

    public MBaggageReclaimOffice(MGeneralRepository MGeneralRepository) {
        NUMBER_OF_LOST_BAGS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }
    
    // only passenger can add
    public SPassenger addBag(int number_of_bags) {       
        lock.lock();
        try{
            // SLEEP
            NUMBER_OF_LOST_BAGS += number_of_bags;
            return goHome();
        }
        catch(Exception e){}
        finally{
            lock.unlock();           
        }
        return null;
    }

    public SPassenger goHome()
    {
        return SPassenger.EXITING_THE_ARRIVAL_TERMINAL;
    }
}