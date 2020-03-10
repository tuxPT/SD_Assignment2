package AirportRapsody.Thread;

import AirportRapsody.State.SPassenger;

public class TPassenger extends Thread {

    SPassenger curState;
    int TOTAL_NUMBER_OF_BAGS;
    int NUMBER_OF_BAGS_RETRIEVED;

    public TPassenger(int numberOfBags)
    {
       curState = STATE.AT_THE_DISEMBARKING_ZONE;
       TOTAL_NUMBER_OF_BAGS = numberOfBags;
       NUMBER_OF_BAGS_RETRIEVED = 0;
    }

    @Override
    public void run() {
        while(!END_OF_DAY) {
            switch(curState) {

            }
        }
    }
}

