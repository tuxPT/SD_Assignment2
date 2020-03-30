package AirportRapsody.Monitor;

import AirportRapsody.Interface.IDepartureTerminalPassenger;
import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.State.SPassenger;

import java.awt.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MDepartureTerminal implements IDepartureTerminalPassenger {
    private IGeneralRepository MGeneralRepository;
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
        lock.lock();
        try{
            lastPassenger.await();            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public Integer getCURRENT_NUMBER_OF_PASSENGERS() {
        return CURRENT_NUMBER_OF_PASSENGERS;
    }

    public SPassenger prepareNextLeg(Integer id) {
        MGeneralRepository.updatePassenger(SPassenger.ENTERING_THE_DEPARTURE_TERMINAL, id, null, null, null, false, null);
        return SPassenger.ENTERING_THE_DEPARTURE_TERMINAL;
    }


    public void lastPassenger(){        
        lock.lock();
        try{
            CURRENT_NUMBER_OF_PASSENGERS = 0;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            lastPassenger.signalAll();
            lock.unlock();
        }
    }
}