package shared_regions_JavaInterfaces;

import common_infrastructures.SPassenger;

public interface IArrivalTerminalTransferQuayPassenger {
    /**
     * Called by a passenger.<br>
     * Passenger will enter the waiting queue.<br>
     * If its place in the waiting queue is equal to the bus capacity (it means there are enough passengers to fill the bus) he'll warn the busdriver.
     * Otherwise he'll wait until the bus driver warns him to enter the bus.<br>
     * After receiving order to enter the bus, if he is the last passenger to enter he'll warn the bus driver that the bus is full so he can start the journey.
     * @param Passenger_ID passenger's ID
     * @return Passenger's state TERMINAL_TRANSFER
     * @see SPassenger
     */
    SPassenger enterTheBus(Integer Passenger_ID);
}
