package shared_regions;

import common_infrastructures.SBusDriver;

public interface IDepartureTerminalTransferQuayBusDriver {
    /**
     * Called by the bus driver.<br>
     * Will warn the passengers that the bus has arrived so they can start disembarking.<br>
     * After being warned by the last passenger that all passengers have disembarked, it returns.
     * @param n number of passengers that were transport by the bus in the current journey.<br>
     * @return Bus driver's state DRIVING_BACKWARD
     * @see SBusDriver
     */
    SBusDriver parkTheBusAndLetPassOff(Integer n);

    /**
     * Called by the driver.<br>
     * Simulates the bus journey.<br>
     * Will wait a bit of time (few milliseconds) and return.
     * @return Bus driver's PARKING_AT_THE_ARRIVAL_TERMINAL
     * @see SBusDriver
     */
    SBusDriver goToArrivalTerminal();
}
