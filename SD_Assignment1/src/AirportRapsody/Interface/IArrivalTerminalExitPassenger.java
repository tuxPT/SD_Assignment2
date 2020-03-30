package AirportRapsody.Interface;

public interface IArrivalTerminalExitPassenger {
    void addPassenger(Integer a);
    void waitingForLastPassenger(); 
    Integer getCURRENT_NUMBER_OF_PASSENGERS();
    void lastPassenger();
}
