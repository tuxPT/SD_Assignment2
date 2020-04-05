package AirportRapsody.Interface;

import AirportRapsody.State.SBusDriver;
import AirportRapsody.State.SPassenger;
import AirportRapsody.State.SPorter;

public interface IGeneralRepository {
    void updateBusDriver(SBusDriver Stat, boolean print);

    void updatePassenger(SPassenger Stat, Integer id, Boolean addWaitingQueue, Boolean addBusSeats, Integer startBags, Boolean collectBags, Boolean transit);

    void updatePorter(SPorter Stat, Integer BN, Integer CB, Integer SR, boolean print);

    void nextFlight();
    void endOfLifePlane();
}
