package shared_regions;

import common_infrastructures.SPassenger;

public interface IDepartureTerminalPassenger {
    /**
     * Called by a passenger.<br>
     * Will increment the number of current passengers in the departure terminal by one.
     * @param id thread number
     * @param curr number of passengers in ArrivalTerminalExit
     * @return true if last passenger, else false
     */
    boolean addPassenger(Integer id, Integer curr);

    /**
     * @return the current number of passengers present in the departure terminal
     */
    Integer getCURRENT_NUMBER_OF_PASSENGERS();

    /**
     * Called if the last plane's passenger to arrive an exit goes to the departure terminal.<br>
     * Warns all passengers that he has arrived so they can all go home or to the next plane.
     */
    void lastPassenger();

    /**
     * @return Passenger's state ENTERING_THE_DEPARTURE_TERMINAL
     * @see SPassenger
     */
    SPassenger prepareNextLeg();
}
