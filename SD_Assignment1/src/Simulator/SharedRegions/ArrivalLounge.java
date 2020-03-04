package Simulator.SharedRegions;

interface ArrivalLoungeInterface {
    static void addPassenger() {};
    static void RemovePassenger() {};    
}

public class ArrivalLounge implements ArrivalLoungeInterface
{
    Integer NUMBER_OF_PASSENGERS;

    public ArrivalLounge() 
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