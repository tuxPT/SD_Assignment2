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

    private Boolean TRANSIT;

    public List<Integer> getBags() {
        return bags;
    }

    public void setBags(List<Integer> bags) {
        this.bags = bags;
    }

    private List<Integer> bags;

    private Integer pthread_number;
    private IArrivalLoungePorter MArrivalLounge;
    private IArrivalTerminalExitPassenger MArrivalTerminalExit;
    private IArrivalTerminalTransferQuayPassenger MArrivalTerminalTransferQuay;
    private IBaggageCollectionPointPassenger MBaggageCollectionPoint;
    private IBaggageReclaimOfficePassenger MBaggageReclaimOffice;
    private IDepartureTerminalTransferQuayPassenger MDepartureTerminalTransferQuay;


    public TPassenger(Integer pthread_number, Integer MAX_BAGS_NUMBER, IArrivalLoungePorter MArrivalLounge, IArrivalTerminalExitPassenger MArrivalTerminalExit, IArrivalTerminalTransferQuayPassenger MArrivalTerminalTransferQuay, IBaggageCollectionPointPassenger MBaggageCollectionPoint, IBaggageReclaimOfficePassenger MBaggageReclaimOffice, IDepartureTerminalTransferQuayPassenger MDepartureTerminalTransferQuay) {
        this.pthread_number = pthread_number;
        this.TRANSIT = random.nextBoolean();
        this.curState = curState.AT_THE_DISEMBARKING_ZONE;
        this.NUMBER_OF_BAGS_RETRIEVED = NUMBER_OF_BAGS_RETRIEVED;
        NUMBER_OF_BAGS_RETRIEVED = 0;
        bags = generateBags(pthread_number, MAX_BAGS_NUMBER);
        this.MArrivalLounge = MArrivalLounge;
        this.MArrivalTerminalExit = MArrivalTerminalExit;
        this.MArrivalTerminalTransferQuay = MArrivalTerminalTransferQuay;
        this.MBaggageCollectionPoint = MBaggageCollectionPoint;
        this.MBaggageReclaimOffice = MBaggageReclaimOffice;
        this.MDepartureTerminalTransferQuay = MDepartureTerminalTransferQuay;
    }


    @Override
    public void run() {
        while(true) {
            switch(curState) {
                case AT_THE_DISEMBARKING_ZONE:
                case AT_THE_LUGGAGE_COLLECTION_POINT:
                case AT_THE_BAGGAGE_RECLAIM_OFFICE:
                case EXITING_THE_ARRIVAL_TERMINAL:
                case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
                case TERMINAL_TRANSFER:
                case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
                case ENTERING_THE_DEPARTURE_TERMINAL:
            }
            Random random = new Random(10);
            try {
                sleep(random.nextInt());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private List<Integer> generateBags(int pthread_number, int MAX_BAGS_NUMBER){
        //gerar numero de malas para o passageiro
        Integer size = random.nextInt(MAX_BAGS_NUMBER+1);

        List<Integer> bags = new ArrayList<Integer>();

        //atribuir um id unico da mala ao passageiro
        for(int nbag=0; nbag<size; nbag++){
            bags.add(pthread_number * MAX_BAGS_NUMBER + nbag);
        }
        return bags;
    }

    public Boolean getTRANSIT() {
        return TRANSIT;
    }
}

