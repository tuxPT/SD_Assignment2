package Simulator.Clients;

class Porter {
    enum STATE {
        WAITING_FOR_A_PLANE_TO_LAND,
        AT_THE_PLANES_HOLD,
        AT_THE_LUGGAGE_BELT_CONVEYOR,
        AT_THE_STOREROOM
    }

    STATE curState;

    public Porter() {
        curState = STATE.WAITING_FOR_A_PLANE_TO_LAND;
    }
    
}