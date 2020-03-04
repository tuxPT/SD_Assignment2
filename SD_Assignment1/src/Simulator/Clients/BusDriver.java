package Simulator.Clients;

class BusDriver {
    enum STATE {
        PARKING_AT_THE_ARRIVAL_TERMINAL,
        DRIVING_FORWARD,
        PARKING_AT_THE_DEPARTURE_TERMINAL,
        DRIVING_BACKWARD
    }
    
    STATE curState;

    public BusDriver() {
        curState = STATE.PARKING_AT_THE_ARRIVAL_TERMINAL;
    }
    
}