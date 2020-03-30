package AirportRapsody.Interface;

import AirportRapsody.State.SPassenger;

public interface IDepartureTerminalPassenger {
    void addPassenger();
    void waitingForLastPassenger();    
    Integer getCURRENT_NUMBER_OF_PASSENGERS();
    void lastPassenger();
    SPassenger prepareNextLeg(Integer pthread_number);
}
