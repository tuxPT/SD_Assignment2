package AirportRapsody.Thread;

import AirportRapsody.Interface.IArrivalLoungePorter;
import AirportRapsody.Interface.IBaggageCollectionPointPorter;
import AirportRapsody.Interface.ITemporaryStorageAreaPorter;
import AirportRapsody.Monitor.Bag;
import AirportRapsody.State.SPorter;

import java.util.Random;

public class TPorter extends Thread {    

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public Integer getPLANES_TO_LAND() {
        return PLANES_TO_LAND;
    }

    public void decrementPLANES_TO_LAND() {
        PLANES_TO_LAND--;
    }      

    private Integer PLANES_TO_LAND;
    private Bag bag;
    private SPorter curState;
    private Integer pthread_number;
    private IArrivalLoungePorter MArrivalLounge;
    private IBaggageCollectionPointPorter MBaggageCollectionPoint;
    private ITemporaryStorageAreaPorter MTemporaryStorageArea;

    public TPorter(Integer pthread_number, Integer PLANES_TO_LAND, IArrivalLoungePorter MArrivalLounge,
            IBaggageCollectionPointPorter MBaggageCollectionPoint, ITemporaryStorageAreaPorter MTemporaryStorageArea) {
        this.PLANES_TO_LAND = PLANES_TO_LAND;
        this.pthread_number = pthread_number;
        this.MArrivalLounge = MArrivalLounge;
        this.MBaggageCollectionPoint = MBaggageCollectionPoint;
        this.MTemporaryStorageArea = MTemporaryStorageArea;
        this.curState = SPorter.WAITING_FOR_A_PLANE_TO_LAND;
    }

    @Override
    public void run() {
        while (PLANES_TO_LAND > 0) {
            switch (curState) {
                case WAITING_FOR_A_PLANE_TO_LAND:
                    curState = MArrivalLounge.takeARest();
                    PLANES_TO_LAND--;                    
                    break;
                case AT_THE_PLANES_HOLD:
                    // TRY TO GET BAG
                    bag = MArrivalLounge.tryToCollectABag();
                    if (bag == null) {
                        MBaggageCollectionPoint.warnPassengers();
                        curState = MArrivalLounge.noMoreBagsToCollect();
                    } else {
                       curState = MArrivalLounge.carryItToAppropriateStore(bag);
                    }
                    break;
                case AT_THE_LUGGAGE_BELT_CONVEYOR:
                    curState = MBaggageCollectionPoint.addBag(bag);                   
                    break;
                case AT_THE_STOREROOM:
                    curState = MTemporaryStorageArea.addBag();                    
                    break;
            }
            //sleep
        }
    }
}
