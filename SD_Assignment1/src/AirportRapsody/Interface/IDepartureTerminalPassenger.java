package AirportRapsody.Interface;

import AirportRapsody.State.SPassenger;

public interface IDepartureTerminalPassenger {
    boolean addPassenger(Integer id, Integer curr);
    void waitingForLastPassenger();    
    Integer getCURRENT_NUMBER_OF_PASSENGERS();
    void lastPassenger();
    SPassenger prepareNextLeg();
}
