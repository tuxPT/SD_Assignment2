package shared_regions_JavaInterfaces;

import common_infrastructures.SBusDriver;
import common_infrastructures.SPassenger;
import common_infrastructures.SPorter;

public interface IGeneralRepository {
    /**
     * Called by the BusDriver.<br>
     * Update BusDriver State
     * @param Stat BusDriver state
     * @param print Print program state if true
     * @see SBusDriver
     */
    void updateBusDriver(SBusDriver Stat, boolean print);

    /**
     * Called by the Passenger.<br>
     * Update Passenger State
     * @param Stat Passenger state
     * @param id thread number
     * @param addWaitingQueue add Passenger id to the Bus waitingQueue if true
     * @param addBusSeats add Passenger id to the Bus Queue if true
     * @param startBags specify the number of bags that the Passenger needs to collect
     * @param collectBags increment number of collect bags if true
     * @param transit specify if Passenger is transit, if true, or nonTransit, if false
     * @see SPassenger
     */
    void updatePassenger(SPassenger Stat, Integer id, Boolean addWaitingQueue, Boolean addBusSeats, Integer startBags, Boolean collectBags, Boolean transit);

    /**
     * Called by the Porter.<br>
     * Update Porter State
     * @param Stat Porter state
     * @param BN number of bags at planes hold or don't update if null
     * @param CB number of bags at conveyor belt or don't update if null
     * @param SR number of bags at the Storeroom or don't update if null
     * @param print print if true
     * @see SPorter
     */
    void updatePorter(SPorter Stat, Integer BN, Integer CB, Integer SR, boolean print);

    /**
     * Called by the AirportRapsody.main.AirportRapsody.<br>
     * Increment flight number and reset passenger stats.
     */
    void nextFlight();

    /**
     * Called by the AirportRapsody.main.AirportRapsody.<br>
     * Updates number of passengers in transit and nontransit and the number of bags lost or transported
     */
    void endOfLifePlane();

    /**
     * Called by the AirportRapsody.main.AirportRapsody.<br>
     * Prints the number of passengers in transit or nontransit and the number of bags lost or transported
     */
    void printRepository();

    /**
     * Atualiza o número de malas no GeneralRepository
     * @param count Número de malas para color no GeneralRepository
     */
    void setBags(int count);
}
