package AirportRapsody.Interface;

public interface IArrivalTerminalExitPassenger {
    void addPassenger();   
    void waitingForLastPassenger(); 
    Integer getCURRENT_NUMBER_OF_PASSENGERS();
    void lastPassenger();
}
