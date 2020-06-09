package shared_regions_JavaInterfaces;

public interface IArrivalTerminalExitPassenger {
    /**
     * Called by a passenger.<br>
     * Will increment the number of current passengers in the arrival terminal exit by one and wait for other passengers to arrive.
     * If it is the last it will signal the other passengers.
     * @param id thread number
     * @param curr number of passengers in DepartureTerminal
     * @return true if he is the last, false if not
     */
    boolean addPassenger(Integer id, Integer curr);

    /**
     * @return the current number of passengers present in the departure terminal
     */
    Integer getCURRENT_NUMBER_OF_PASSENGERS();

    /**
     * Called if the last plane's passenger to arrive an exit goes to the arrival terminal exit.<br>
     * Warns all passengers that he has arrived so they can all go home or to the next plane.
     */
    void lastPassenger();
}
