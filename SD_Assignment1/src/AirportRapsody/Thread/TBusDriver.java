package AirportRapsody.Thread;

import AirportRapsody.Interface.*;
import AirportRapsody.State.SBusDriver;

public class TBusDriver extends Thread {

    
    private SBusDriver curState;
    private boolean endOfDay;

    private Integer numberPassengersOnBus;
    private Integer pthread_number;
    private IArrivalTerminalTransferQuayBusDriver MArrivalTerminalTransferQuayBusDriver;
    private IDepartureTerminalTransferQuayBusDriver MDepartureTerminalTransferQuayBusDriver;

    public TBusDriver(Integer pthread_number, IArrivalTerminalTransferQuayBusDriver MArrivalTerminalTransferQuayBusDriver, IDepartureTerminalTransferQuayBusDriver MDepartureTerminalTransferQuayBusDriver) {
        this.curState = SBusDriver.PARKING_AT_THE_ARRIVAL_TERMINAL;
        this.pthread_number = pthread_number;
        this.MArrivalTerminalTransferQuayBusDriver = MArrivalTerminalTransferQuayBusDriver;
        this.MDepartureTerminalTransferQuayBusDriver = MDepartureTerminalTransferQuayBusDriver;
        this.numberPassengersOnBus = 0;
        this.endOfDay = false;
    }

    @Override
    public void run() {
        while(!endOfDay) {
            switch(curState) {
                case PARKING_AT_THE_ARRIVAL_TERMINAL:
                    if(MArrivalTerminalTransferQuayBusDriver.announcingBusBoarding()){
                        endOfDay = true;
                    }
                    else{
                        curState = SBusDriver.DRIVING_FORWARD;
                    }
                break;
                case DRIVING_FORWARD:
                    numberPassengersOnBus = MArrivalTerminalTransferQuayBusDriver.goToDepartureTerminal();
                    curState = SBusDriver.PARKING_AT_THE_DEPARTURE_TERMINAL;
                break;
                case PARKING_AT_THE_DEPARTURE_TERMINAL:
                    curState = MDepartureTerminalTransferQuayBusDriver.parkTheBusAndLetPassOff(numberPassengersOnBus);
                break;
                case DRIVING_BACKWARD:
                    curState = MDepartureTerminalTransferQuayBusDriver.goToArrivalTerminal();
                break;
            }
            // SLEEP            
        }
    }
    public void setEndOfDay(){
        endOfDay = true;
    }
}

