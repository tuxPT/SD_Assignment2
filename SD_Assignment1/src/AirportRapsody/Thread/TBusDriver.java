package AirportRapsody.Thread;

import AirportRapsody.State.SBusDriver;

public class TBusDriver extends Thread {

    
    SBusDriver curState;

    public TBusDriver() {
        curState = STATE.PARKING_AT_THE_ARRIVAL_TERMINAL;
    }

    @Override
    public void run() {
        while(!END_OF_DAY) {
            switch(curState) {

            }
        }
    }
}

