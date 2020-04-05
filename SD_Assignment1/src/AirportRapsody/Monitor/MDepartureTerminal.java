package AirportRapsody.Monitor;

import AirportRapsody.Interface.IDepartureTerminalPassenger;
import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.State.SPassenger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MDepartureTerminal implements IDepartureTerminalPassenger {
    private IGeneralRepository MGeneralRepository;
    Integer CURRENT_NUMBER_OF_PASSENGERS;
    private Integer PLANE_PASSENGERS;
    ReentrantLock lock = new ReentrantLock(true);
    Condition lastPassenger = lock.newCondition();

    /**
     * @param MGeneralRepository The General Repository used for logging   
     */
    public MDepartureTerminal(Integer PLANE_PASSENGERS, MGeneralRepository MGeneralRepository)
    {
        CURRENT_NUMBER_OF_PASSENGERS = 0;
        this.MGeneralRepository = MGeneralRepository;
        this.PLANE_PASSENGERS = PLANE_PASSENGERS;
    }

    /**
     * Called by a passenger.<br/>
     * Will increment the number of current passengers in the departure terminal by one.
     * @return
     */
    public boolean addPassenger(Integer id, Integer current_arrival)
    {
        lock.lock();
        boolean tmp = false;
        try {
            CURRENT_NUMBER_OF_PASSENGERS++;
            MGeneralRepository.updatePassenger(SPassenger.ENTERING_THE_DEPARTURE_TERMINAL, id, null, null, null, false, null);
            if(current_arrival + CURRENT_NUMBER_OF_PASSENGERS == PLANE_PASSENGERS){
                tmp = true;
            }
            else{
                lastPassenger.await();
                tmp = false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return tmp;
    }

    /**
     * Called by a passenger.<br/>
     * After entering the departure terminal, the passenger will wait for the last passenger to arrive (either the arrival terminal exit
     * or the departure terminal) so he can continue his journey.
     */
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

    /**
     * @return the current number of passengers present in the departure terminal
     */
    public Integer getCURRENT_NUMBER_OF_PASSENGERS() {
        lock.lock();
        Integer tmp = 0;
        try{
            tmp = CURRENT_NUMBER_OF_PASSENGERS;
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            lock.unlock();
        }
        return tmp;
    }

    /**
     * @return Passenger's state ENTERING_THE_DEPARTURE_TERMINAL
     * @see SPassenger
     */
    public SPassenger prepareNextLeg() {
        return SPassenger.ENTERING_THE_DEPARTURE_TERMINAL;
    } 
    
    /**
     * Called if the last plane's passenger to arrive an exit goes to the departure terminal.<br/>
     * Warns all passengers that he has arrived so they can all go home or to the next plane.
     */
    public void lastPassenger(){        
        lock.lock();
        try{
            lastPassenger.signalAll();
            CURRENT_NUMBER_OF_PASSENGERS = 0;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
    }
}