package AirportRapsody.Thread;

import AirportRapsody.State.SPorter;

public class TPorter extends Thread {


    SPorter curState;

    public TPorter() {
        curState = STATE.WAITING_FOR_A_PLANE_TO_LAND;
    }

    @Override
    public void run() {
        while(!END_OF_DAY) {
            switch(curState) {

            }
        }

    }
}

