package Simulator.SharedRegions;

interface BaggageReclaimOfficeInterface {
    static void addBag() {
    };
}

public class BaggageReclaimOffice implements BaggageReclaimOfficeInterface {
    Integer NUMBER_OF_LOST_BAGS;

        

    public BaggageReclaimOffice() {
        NUMBER_OF_LOST_BAGS = 0;
    }
    
    // only passenger can add
    public void addBag() {
        NUMBER_OF_LOST_BAGS++;
    }
}