package Simulator.SharedRegions;

interface TemporaryStorageAreaInterface {
    static void addBag() {
    };
}

public class TemporaryStorageArea implements TemporaryStorageAreaInterface {
    Integer NUMBER_OF_BAGS;

    public TemporaryStorageArea() {
        NUMBER_OF_BAGS = 0;
    }

    // only porter can add
    public void addBag() {
        NUMBER_OF_BAGS++;
    }
}