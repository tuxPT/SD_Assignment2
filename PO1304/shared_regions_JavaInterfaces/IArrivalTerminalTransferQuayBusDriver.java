package shared_regions_JavaInterfaces;

import common_infrastructures.SBusDriver;

public interface IArrivalTerminalTransferQuayBusDriver {
    /**
     * Called by the bus driver.<br>
     * The bus driver order the passengers to board the bus when there are enough passengers in queue
     * to fill the whole bus or after a certain time (each few milliseconds, if there is at least
     * one passenger in the waiting queue) to simulate a schedule.<br>
     * After the passengers starts embarking, the bus driver will wait until the last one to embark
     * warns them that all passengers are aboard.
     * @return BusDriver's state DRIVING_FORWARD
     * @see SBusDriver
     */
    boolean announcingBusBoarding();

    /**
     * Called by the driver.<br>
     * Simulates the bus journey.<br>
     * Will wait a bit of time (few milliseconds) and return.
     * @return the number of passengers that embarked the bus
     */
    Integer goToDepartureTerminal();


    void endOfWork();
}
