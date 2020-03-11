package AirportRapsody.Thread;

import AirportRapsody.Interface.IArrivalLoungePorter;
import AirportRapsody.Interface.IBaggageCollectionPointPorter;
import AirportRapsody.Interface.ITemporaryStorageAreaPorter;
import AirportRapsody.State.SPorter;

import java.util.Random;

public class TPorter extends Thread {
    SPorter curState;

    public TPorter(int thread_number,
                   IArrivalLoungePorter MArrivalLounge,
                   IBaggageCollectionPointPorter MBaggageCollectionPoint,
                   ITemporaryStorageAreaPorter MTemporaryStorageArea
    ) {
        curState = curState.WAITING_FOR_A_PLANE_TO_LAND;
    }

    @Override
    public void run() {
        while(true) {
            switch(curState) {
                case WAITING_FOR_A_PLANE_TO_LAND:
                case AT_THE_PLANES_HOLD:
                case AT_THE_LUGGAGE_BELT_CONVEYOR:
                case AT_THE_STOREROOM:
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

