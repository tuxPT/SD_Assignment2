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

    public MArrivalTerminalExit(MGeneralRepository MGeneralRepository)
    {        
        CURRENT_NUMBER_OF_PASSENGERS = 0;
        this.MGeneralRepository = MGeneralRepository;
    }

    // PASSENGER
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

    // PASSENGER
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

    public Integer getCURRENT_NUMBER_OF_PASSENGERS() {
        return CURRENT_NUMBER_OF_PASSENGERS;
    }

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
