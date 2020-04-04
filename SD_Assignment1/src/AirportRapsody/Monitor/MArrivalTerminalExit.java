package AirportRapsody.Monitor;

import AirportRapsody.Interface.IArrivalTerminalExitPassenger;
import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.State.SPassenger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MArrivalTerminalExit implements IArrivalTerminalExitPassenger {
    private IGeneralRepository MGeneralRepository;
    Integer CURRENT_NUMBER_OF_PASSENGERS;

    ReentrantLock lock = new ReentrantLock(true);
    Condition lastPassenger = lock.newCondition();

    /**
     * @param MGeneralRepository The General Repository used for logging   
     */
    public MArrivalTerminalExit(MGeneralRepository MGeneralRepository)
    {        
        CURRENT_NUMBER_OF_PASSENGERS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }

    /**
     * Called by a passenger.<br/>
     * Will increment the number of current passengers in the arrival terminal exit by one.
     */
    public void addPassenger(Integer id)
    {
        assert id != null : "Thread_id não especificado";
        lock.lock();
        try {
            CURRENT_NUMBER_OF_PASSENGERS++;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            MGeneralRepository.updatePassenger(SPassenger.EXITING_THE_ARRIVAL_TERMINAL, id, null, null, null, false, null);
            lock.unlock();
        }

    }

    /**
     * Called by a passenger.<br/>
     * After entering the arrival terminal exit, the passenger will wait for the last passenger to arrive (either the arrival terminal exit
     * or the departure terminal) so he can go home.
     */
    public void waitingForLastPassenger()
    {           
        lock.lock();
        try{
            lastPassenger.await();            
        }
        catch(Exception e) {}
        finally{
            lock.unlock();
        }
    }

    /**
     * @return the current number of passengers present in the departure terminal
     */
    public Integer getCURRENT_NUMBER_OF_PASSENGERS() {
        return CURRENT_NUMBER_OF_PASSENGERS;
    }

     /**
     * Called if the last plane's passenger to arrive an exit goes to the arrival terminal exit.<br/>
     * Warns all passengers that he has arrived so they can all go home or to the next plane.
     */
    public void lastPassenger(){
        lock.lock();
        lastPassenger.signalAll();
        try{
            // SLEEP
            CURRENT_NUMBER_OF_PASSENGERS = 0;
        }
        catch(Exception e) {}
        finally{
            lock.unlock();
        }
    }
}
