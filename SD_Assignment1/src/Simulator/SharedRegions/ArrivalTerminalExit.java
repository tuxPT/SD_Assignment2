package Simulator.SharedRegions;

interface ArrivalTerminalExitInterface {
    static void addPassenger() {};
    static void RemovePassenger() {};    
}

public class ArrivalTerminalExit implements ArrivalTerminalExitInterface {
    Integer NUMBER_OF_PASSENGERS;

    public ArrivalTerminalExit() 
    {
        NUMBER_OF_PASSENGERS = 0;
    }

    // PASSENGER
    public void addPassenger()
    {
        NUMBER_OF_PASSENGERS++;
    }

    // PASSENGER
    public void RemovePassenger()
    {
        NUMBER_OF_PASSENGERS--;
    }
}