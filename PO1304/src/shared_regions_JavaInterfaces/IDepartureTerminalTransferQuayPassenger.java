package shared_regions_JavaInterfaces;

import common_infrastructures.SPassenger;

public interface IDepartureTerminalTransferQuayPassenger {

    /**
     * Called by a passenger.<br>
     * Passenger will wait until being warned by the bus driver that the bus has arrived so he can disembark.<br>
     * If he is the last passenger disembarking he'll warn the bus driver so he can go back to the arrival terminal.
     * @param id passenger's id
     * @return Passenger's state AT_THE_DEPARTURE_TRANSFER_TERMINAL
     * @see SPassenger
     */
    SPassenger leaveTheBus(Integer id);
}
