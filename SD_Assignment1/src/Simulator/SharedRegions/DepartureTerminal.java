package Simulator.SharedRegions;

interface DepartureTerminalInterface {
    static void addPassenger() {};
    static void RemovePassenger() {};    
}

public class DepartureTerminal implements DepartureTerminalInterface {
    Integer NUMBER_OF_PASSENGERS;

    public DepartureTerminal() 
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