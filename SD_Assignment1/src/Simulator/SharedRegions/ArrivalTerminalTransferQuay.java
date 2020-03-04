package Simulator.SharedRegions;

import java.util.LinkedList;
import java.util.Queue;

interface ArrivalTerminalTransferQuayInterface {
    static void push() {};
    static void pop() {};
}

public class ArrivalTerminalTransferQuay implements ArrivalTerminalTransferQuayInterface {
    Queue<Integer> BOARDING_QUEUE;


    public ArrivalTerminalTransferQuay() {
        BOARDING_QUEUE = new LinkedList<Integer>();
    }

    public void push(Integer passenger) {
        BOARDING_QUEUE.add(passenger);
    }

    public void pop() {
        BOARDING_QUEUE.remove();
    }
}