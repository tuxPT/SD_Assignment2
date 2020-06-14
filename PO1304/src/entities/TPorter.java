package entities;

import common_infrastructures.Bag;
import common_infrastructures.SPorter;
import shared_regions_JavaInterfaces.IArrivalLoungePorter;
import shared_regions_JavaInterfaces.IBaggageCollectionPointPorter;
import shared_regions_JavaInterfaces.ITemporaryStorageAreaPorter;

public class TPorter extends Thread {
    private boolean endOfDay;

    private Bag bag;
    private SPorter curState;
    private Integer pthread_number;
    private IArrivalLoungePorter MArrivalLounge;
    private IBaggageCollectionPointPorter MBaggageCollectionPoint;
    private ITemporaryStorageAreaPorter MTemporaryStorageArea;

    public TPorter(Integer pthread_number, IArrivalLoungePorter MArrivalLounge,
            IBaggageCollectionPointPorter MBaggageCollectionPoint, ITemporaryStorageAreaPorter MTemporaryStorageArea) {
        this.pthread_number = pthread_number;
        this.MArrivalLounge = MArrivalLounge;
        this.MBaggageCollectionPoint = MBaggageCollectionPoint;
        this.MTemporaryStorageArea = MTemporaryStorageArea;
        this.curState = SPorter.WAITING_FOR_A_PLANE_TO_LAND;
        this.endOfDay = false;
    }

    @Override
    public void run() {
        while (!endOfDay) {
            switch (curState) {
                case WAITING_FOR_A_PLANE_TO_LAND:
                    if(MArrivalLounge.takeARest()){
                        endOfDay = true;
                    }
                    else{
                        curState = SPorter.AT_THE_PLANES_HOLD;
                        System.out.println("ATPLH");
                    }
                    break;
                case AT_THE_PLANES_HOLD:
                    // TRY TO GET BAG
                    bag = MArrivalLounge.tryToCollectABag();
                    System.out.println("TRY_COLLECT");
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
        }
    }

    public void setEndOfDay() {
        endOfDay = true;
    }
}
