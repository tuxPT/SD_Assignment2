package shared_regions;

import common_infrastructures.SPassenger;

public interface IArrivalLoungePassenger {
    /**
     * Called by the passengers in the beggining of their cycle (AT_THE_DISEMBARKING_ZONE).<br>
     * Simulates their arrival to the Arrival Lounge.<br>
     * Passengers will wait here for every passenger to get to the arrival lounge.<br>
     * Last passenger to arrive warns the rest and they all get out.<br>
     * Function will return one of the following passenger's states:<br>
     * AT_THE_ARRIVAL_TRANSFER_TERMINAL, if the passenger is in transit;<br>
     * AT_THE_LUGGAGE_COLLECTION_POINT, if the passenger is in the final destination and he has bags to collect;<br>
     * EXITING_THE_ARRIVAL_TERMINAL, if the passenger is in the final destination but he has no bags to collect<br>
     * @param id passenger's id (1 up to 6)
     * @param t_bags number of bags the passenger had at the beggining of the journey
     * @param t_TRANSIT true if the passenger is in transit, false otherwise
     * @return Passenger's state AT_THE_ARRIVAL_TRANSFER_TERMINAL, AT_THE_LUGGAGE_COLLECTION_POINT or EXITING_THE_ARRIVAL_TERMINAL
     * @see SPassenger
     */
    SPassenger whatShouldIDo(Integer id, Integer t_bags, boolean t_TRANSIT);
}
