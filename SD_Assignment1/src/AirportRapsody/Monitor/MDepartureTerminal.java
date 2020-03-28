package AirportRapsody.Monitor;

import AirportRapsody.Interface.IDepartureTerminalPassenger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MDepartureTerminal implements IDepartureTerminalPassenger {
    private MGeneralRepository MGeneralRepository;
    Integer CURRENT_NUMBER_OF_PASSENGERS;

    ReentrantLock lock = new ReentrantLock(true);
    Condition lastPassenger = lock.newCondition();

    public MDepartureTerminal(MGeneralRepository MGeneralRepository)
    {
        CURRENT_NUMBER_OF_PASSENGERS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }

    // PASSENGER
    public void addPassenger()
    {
        CURRENT_NUMBER_OF_PASSENGERS++;
    }

    // PASSENGER
    public void waitingForLastPassenger()
    {
        try{
            lastPassenger.await();            
        }
        catch(Exception e) {}   
    }

    public Integer getCURRENT_NUMBER_OF_PASSENGERS() {
        return CURRENT_NUMBER_OF_PASSENGERS;
    }        

    public void lastPassenger(){        
        lastPassenger.signalAll();
        lock.lock();
        try{
            CURRENT_NUMBER_OF_PASSENGERS = 0;
        }
        catch(Exception e) {}
        finally{
            lock.unlock();
        }
    }    
}