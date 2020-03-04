package Simulator.SharedRegions;

interface DepartureTerminalTransferQuayInterface {
    static void increment() {};
    static void reset() {};
}

public class DepartureTerminalTransferQuay implements DepartureTerminalTransferQuayInterface {
    Integer DISEMBARKED_PASSENGERS;

    public DepartureTerminalTransferQuay() {
        DISEMBARKED_PASSENGERS = 0;
    }
    
    public Integer increment() {
        return DISEMBARKED_PASSENGERS++;
    }

    public void reset() {
        DISEMBARKED_PASSENGERS = 0;
    }
}