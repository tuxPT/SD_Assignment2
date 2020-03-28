package AirportRapsody.Thread;

import AirportRapsody.Interface.*;
import AirportRapsody.State.SPassenger;

import java.util.*;

public class TPassenger extends Thread {
    private Random random = new Random();

    public void setCurState(SPassenger curState) {
        this.curState = curState;
    }

    private SPassenger curState;
    private int NUMBER_OF_BAGS_RETRIEVED;

    public int getNUMBER_OF_BAGS_RETRIEVED() {
        return NUMBER_OF_BAGS_RETRIEVED;
    }

    public void setNUMBER_OF_BAGS_RETRIEVED(int nUMBER_OF_BAGS_RETRIEVED) {
        NUMBER_OF_BAGS_RETRIEVED = nUMBER_OF_BAGS_RETRIEVED;
    }

    private boolean endOfLife;
    private Boolean TRANSIT;

    public List<Integer> getBags() {
        return bags;
    }

    public void setBags(List<Integer> bags) {
        this.bags = bags;
    }

    public Boolean getTRANSIT() {
        return TRANSIT;
    }

    public Integer getPthread_number() {
        return pthread_number;
    }    

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
            IArrivalLoungePassenger MArrivalLounge, IArrivalTerminalExitPassenger MArrivalTerminalExit,
            IArrivalTerminalTransferQuayPassenger MArrivalTerminalTransferQuay,
            IBaggageCollectionPointPassenger MBaggageCollectionPoint,
            IBaggageReclaimOfficePassenger MBaggageReclaimOffice,
            IDepartureTerminalTransferQuayPassenger MDepartureTerminalTransferQuay,
            IDepartureTerminalPassenger MDepartureTerminal) {

        this.endOfLife = false;
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
                    curState = MArrivalLounge.whatShouldIDo(bags,TRANSIT);
                    break;
                case AT_THE_LUGGAGE_COLLECTION_POINT:
                    NUMBER_OF_BAGS_RETRIEVED = MBaggageCollectionPoint.goCollectABag(bags);
                    if (bags.size() == NUMBER_OF_BAGS_RETRIEVED){
                        curState = goHome();
                    }
                    else{
                        curState = reportMissingsBags();
                    }
                    break;
                case AT_THE_BAGGAGE_RECLAIM_OFFICE:
                    curState = MBaggageReclaimOffice.addBag(bags.size() - NUMBER_OF_BAGS_RETRIEVED);
                    break;
                case EXITING_THE_ARRIVAL_TERMINAL:
                    MArrivalTerminalExit.addPassenger();
                    if (PASSENGERS_PER_PLANE == MArrivalTerminalExit.getCURRENT_NUMBER_OF_PASSENGERS()
                            + MDepartureTerminal.getCURRENT_NUMBER_OF_PASSENGERS()) {
                        MArrivalTerminalExit.lastPassenger();
                        MDepartureTerminal.lastPassenger();
                    } else {
                        MArrivalTerminalExit.waitingForLastPassenger();
                    }
                    endOfLife = true;
                    break;

                case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
                    curState = MArrivalTerminalTransferQuay.enterTheBus(pthread_number);
                    break;
                case TERMINAL_TRANSFER:
                    curState = MDepartureTerminalTransferQuay.leaveTheBus();
                    break;
                case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
                    curState = prepareNextLeg();
                    break;
                case ENTERING_THE_DEPARTURE_TERMINAL:
                    MDepartureTerminal.addPassenger();
                    if (PASSENGERS_PER_PLANE == MArrivalTerminalExit.getCURRENT_NUMBER_OF_PASSENGERS()
                            + MDepartureTerminal.getCURRENT_NUMBER_OF_PASSENGERS()) {
                        MArrivalTerminalExit.lastPassenger();
                        MDepartureTerminal.lastPassenger();
                    } else {
                        MDepartureTerminal.waitingForLastPassenger();
                    }
                    endOfLife = true;
                    break;
            }           
        }
    }

    private SPassenger prepareNextLeg() {
        return SPassenger.ENTERING_THE_DEPARTURE_TERMINAL;
    }


}
