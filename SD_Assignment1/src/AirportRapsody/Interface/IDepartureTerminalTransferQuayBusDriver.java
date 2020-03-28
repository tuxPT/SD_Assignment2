package AirportRapsody.Interface;

import AirportRapsody.State.SBusDriver;

public interface IDepartureTerminalTransferQuayBusDriver {
    SBusDriver parkTheBusAndLetPassOff(Integer n);
    SBusDriver goToArrivalTerminal();
}
