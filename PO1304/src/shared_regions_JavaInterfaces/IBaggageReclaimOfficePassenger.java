package shared_regions_JavaInterfaces;

import common_infrastructures.SPassenger;

public interface IBaggageReclaimOfficePassenger {
    /**
     * Called by a passenger.<br>
     * Adds to the total number of lost bags the number of lost bags that the passenger has lost.
     @param id passenger's id
     @param i number of bags that the current passenger lost
     @return Passenger's state EXITING_THE_ARRIVAL_TERMINAL
     @see SPassenger
     */
    SPassenger addBag(Integer id, int i);
}
