package AirportRapsody.Monitor;

import AirportRapsody.Interface.IArrivalTerminalTransferQuayBusDriver;
import AirportRapsody.Interface.IArrivalTerminalTransferQuayPassenger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class MArrivalTerminalTransferQuay implements IArrivalTerminalTransferQuayBusDriver, IArrivalTerminalTransferQuayPassenger {
    Queue<Integer> BOARDING_QUEUE;

    ReentrantLock lock = new ReentrantLock();

    public MArrivalTerminalTransferQuay() {
        BOARDING_QUEUE = new LinkedList<Integer>();
    }

    public void push(Integer passenger) {
        BOARDING_QUEUE.add(passenger);
    }

    public void pop() {
        BOARDING_QUEUE.remove();
    }

    @Override
    public void announcingBusBoarding() {

    }

    @Override
    public void goToDepartureTerminal() {

    }

    @Override
    public void takeABus() {

    }

    @Override
    public void enterTheBus() {

    }
}