package entities;

import java.util.List;
import java.util.Random;

import common_infrastructures.SPassenger;
import shared_regions_JavaInterfaces.IArrivalLoungePassenger;
import shared_regions_JavaInterfaces.IArrivalTerminalExitPassenger;
import shared_regions_JavaInterfaces.IArrivalTerminalTransferQuayPassenger;
import shared_regions_JavaInterfaces.IBaggageCollectionPointPassenger;
import shared_regions_JavaInterfaces.IBaggageReclaimOfficePassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalPassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalTransferQuayPassenger;

public class TPassenger extends Thread {
    private Random random = new Random();

    private SPassenger curState;
    private int NUMBER_OF_BAGS_RETRIEVED;

    private boolean endOfLife;
    private Boolean TRANSIT;

    private boolean tmp;

    public SPassenger goHome()
    {
       return SPassenger.EXITING_THE_ARRIVAL_TERMINAL;
    }

    public SPassenger reportMissingsBags()
    {
       return SPassenger.AT_THE_BAGGAGE_RECLAIM_OFFICE;
    }

    private List<Integer> bags;

    private Integer PASSENGERS_PER_PLANE;
    private Integer pthread_number;
    private IArrivalLoungePassenger MArrivalLounge;
    private IArrivalTerminalExitPassenger MArrivalTerminalExit;
    private IArrivalTerminalTransferQuayPassenger MArrivalTerminalTransferQuay;
    private IBaggageCollectionPointPassenger MBaggageCollectionPoint;
    private IBaggageReclaimOfficePassenger MBaggageReclaimOffice;
    private IDepartureTerminalTransferQuayPassenger MDepartureTerminalTransferQuay;
    private IDepartureTerminalPassenger MDepartureTerminal;

    public TPassenger(Integer pthread_number, boolean TRANSIT,
                      List<Integer> temp, Integer PLANE_PASSENGERS, IArrivalLoungePassenger MArrivalLounge, IArrivalTerminalExitPassenger MArrivalTerminalExit,
                      IArrivalTerminalTransferQuayPassenger MArrivalTerminalTransferQuay,
                      IBaggageCollectionPointPassenger MBaggageCollectionPoint,
                      IBaggageReclaimOfficePassenger MBaggageReclaimOffice,
                      IDepartureTerminalTransferQuayPassenger MDepartureTerminalTransferQuay,
                      IDepartureTerminalPassenger MDepartureTerminal) {

        this.endOfLife = false;
        this.PASSENGERS_PER_PLANE = PLANE_PASSENGERS;
        this.bags = temp;
        this.pthread_number = pthread_number;
        this.TRANSIT = random.nextBoolean();
        this.curState = SPassenger.AT_THE_DISEMBARKING_ZONE;
        this.NUMBER_OF_BAGS_RETRIEVED = 0;
        this.MArrivalLounge = MArrivalLounge;
        this.MArrivalTerminalExit = MArrivalTerminalExit;
        this.MArrivalTerminalTransferQuay = MArrivalTerminalTransferQuay;
        this.MBaggageCollectionPoint = MBaggageCollectionPoint;
        this.MBaggageReclaimOffice = MBaggageReclaimOffice;
        this.MDepartureTerminalTransferQuay = MDepartureTerminalTransferQuay;
        this.MDepartureTerminal = MDepartureTerminal;
        this.TRANSIT = TRANSIT;
    }

    @Override
    public void run() {
        while (!endOfLife) {
            switch (curState) {
                case AT_THE_DISEMBARKING_ZONE:
                    curState = MArrivalLounge.whatShouldIDo(pthread_number, bags.size(), TRANSIT);
                    break;
                case AT_THE_LUGGAGE_COLLECTION_POINT:
                    NUMBER_OF_BAGS_RETRIEVED = MBaggageCollectionPoint.goCollectABag(pthread_number, bags);
                    if (bags.size() == NUMBER_OF_BAGS_RETRIEVED){
                        curState = goHome();
                    }
                    else{
                        curState = reportMissingsBags();
                    }
                    break;
                case AT_THE_BAGGAGE_RECLAIM_OFFICE:
                    curState = MBaggageReclaimOffice.addBag(pthread_number, bags.size() - NUMBER_OF_BAGS_RETRIEVED);
                    break;
                case EXITING_THE_ARRIVAL_TERMINAL:
                    tmp = MArrivalTerminalExit.addPassenger(pthread_number, MDepartureTerminal.getCURRENT_NUMBER_OF_PASSENGERS());
                    if (tmp) {
                        MArrivalTerminalExit.lastPassenger();
                        MDepartureTerminal.lastPassenger();
                    }
                    endOfLife = true;
                    break;

                case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
                    curState = MArrivalTerminalTransferQuay.enterTheBus(pthread_number);
                    break;
                case TERMINAL_TRANSFER:
                    curState = MDepartureTerminalTransferQuay.leaveTheBus(pthread_number);
                    break;
                case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
                    curState = MDepartureTerminal.prepareNextLeg();
                    break;
                case ENTERING_THE_DEPARTURE_TERMINAL:
                    tmp = MDepartureTerminal.addPassenger(pthread_number, MArrivalTerminalExit.getCURRENT_NUMBER_OF_PASSENGERS());
                    if (tmp){
                        MArrivalTerminalExit.lastPassenger();
                        MDepartureTerminal.lastPassenger();
                    }
                    endOfLife = true;
                    break;
            }           
        }
    }
}
