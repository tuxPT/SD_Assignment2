package AirportRapsody.Interface;

import AirportRapsody.State.SBusDriver;

public interface IArrivalTerminalTransferQuayBusDriver {
    SBusDriver announcingBusBoarding();
    Integer goToDepartureTerminal();
}
