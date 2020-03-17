package AirportRapsody.Thread;

import AirportRapsody.Interface.IArrivalLoungePorter;
import AirportRapsody.Interface.IBaggageCollectionPointPorter;
import AirportRapsody.Interface.ITemporaryStorageAreaPorter;
import AirportRapsody.Monitor.Bag;
import AirportRapsody.State.SPorter;

import java.util.Random;

public class TPorter extends Thread {
    public void setCurState(SPorter curState) {
        this.curState = curState;
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    private Bag bag;
    private SPorter curState;
    private Integer pthread_number;
    private IArrivalLoungePorter MArrivalLounge;
    private IBaggageCollectionPointPorter MBaggageCollectionPoint;
    private ITemporaryStorageAreaPorter MTemporaryStorageArea;

    public TPorter(Integer pthread_number,
                   IArrivalLoungePorter MArrivalLounge,
                   IBaggageCollectionPointPorter MBaggageCollectionPoint,
                   ITemporaryStorageAreaPorter MTemporaryStorageArea
    ) {
        this.pthread_number = pthread_number;
        this.MArrivalLounge = MArrivalLounge;
        this.MBaggageCollectionPoint = MBaggageCollectionPoint;
        this.MTemporaryStorageArea = MTemporaryStorageArea;
        this.curState = curState.WAITING_FOR_A_PLANE_TO_LAND;
    }

    @Override
    public void run() {
        while(true) {
            switch(curState) {
                case WAITING_FOR_A_PLANE_TO_LAND:
                    MArrivalLounge.takeARest();
                case AT_THE_PLANES_HOLD:
                    MArrivalLounge.tryToCollectABag();
                    if(bag == null){
                        MArrivalLounge.noMoreBagsToCollect();
                    }
                    else{
                        MArrivalLounge.carryItToAppropriateStore(bag);
                    }
                case AT_THE_LUGGAGE_BELT_CONVEYOR:
                    MBaggageCollectionPoint.addBag(bag);
                case AT_THE_STOREROOM:
                    MTemporaryStorageArea.addBag();

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

