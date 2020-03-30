package AirportRapsody.Interface;

import AirportRapsody.State.SBusDriver;
import AirportRapsody.State.SPassenger;
import AirportRapsody.State.SPorter;

public interface IGeneralRepository {
    void updatePorter(SPorter Stat, Integer BN, Integer CB, Integer SR);
    void updatePassenger(SPassenger Stat, Integer id, Boolean addWaitingQueue, Boolean addBusSeats, Integer startBags, Boolean collectBags, Boolean transit);
    void updateBusDriver(SBusDriver Stat);
    void nextFlight();
}
