package AirportRapsody.Interface;

public interface IArrivalTerminalExitPassenger {
    boolean addPassenger(Integer id, Integer curr);
    void waitingForLastPassenger(); 
    Integer getCURRENT_NUMBER_OF_PASSENGERS();
    void lastPassenger();
}
