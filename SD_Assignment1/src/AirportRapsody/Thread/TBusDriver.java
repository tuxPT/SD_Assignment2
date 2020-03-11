package AirportRapsody.Thread;

import AirportRapsody.Interface.*;
import AirportRapsody.State.SBusDriver;

import java.util.Random;

public class TBusDriver extends Thread {

    
    SBusDriver curState;

    public TBusDriver(Integer thread_number,
                      IArrivalTerminalTransferQuayBusDriver MArrivalTerminalTransferQuayBusDriver,
                      IDepartureTerminalTransferQuayBusDriver MDepartureTerminalTransferQuayBusDriver) {
        curState = curState.PARKING_AT_THE_ARRIVAL_TERMINAL;
    }

    @Override
    public void run() {
        while(true) {
            switch(curState) {
                case PARKING_AT_THE_ARRIVAL_TERMINAL:
                case DRIVING_FORWARD:
                case PARKING_AT_THE_DEPARTURE_TERMINAL:
                case DRIVING_BACKWARD:
            }
            Random random = new Random(10);
            try {
                sleep(random.nextInt());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

