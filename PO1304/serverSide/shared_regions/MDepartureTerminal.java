package serverSide.shared_regions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import common_infrastructures.SPassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalPassenger;
import shared_regions_JavaInterfaces.IGeneralRepository;

public class MDepartureTerminal implements IDepartureTerminalPassenger {
    private IGeneralRepository MGeneralRepository;
    Integer CURRENT_NUMBER_OF_PASSENGERS;
    private Integer PLANE_PASSENGERS;
    ReentrantLock lock = new ReentrantLock(true);
    Condition lastPassenger = lock.newCondition();

    /**
     * @param PLANE_PASSENGERS number of passengers in a plane
     * @param MGeneralRepository The General Repository used for logging   
     */
    public MDepartureTerminal(Integer PLANE_PASSENGERS, IGeneralRepository MGeneralRepository)
    {
        CURRENT_NUMBER_OF_PASSENGERS = 0;
        this.MGeneralRepository = MGeneralRepository;
        this.PLANE_PASSENGERS = PLANE_PASSENGERS;
    }


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


    public SPassenger prepareNextLeg() {
        return SPassenger.ENTERING_THE_DEPARTURE_TERMINAL;
    } 
    

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