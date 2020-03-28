import AirportRapsody.Interface.*;
import AirportRapsody.Monitor.*;
import AirportRapsody.Thread.TBusDriver;
import AirportRapsody.Thread.TPassenger;
import AirportRapsody.Thread.TPorter;

import java.util.ArrayList;
import java.util.Random;

public class AirportRapsody {

    public static void main(String[] args) throws Exception {
        ArrayList<Bag>[] bags;
        Integer PLANE_PASSENGERS, MAX_PORTER, MAX_BUSDRIVER, PLANES_PER_DAY, MAX_BAGS_NUMBER,BUS_CAPACITY;
        PLANE_PASSENGERS = 6;
        MAX_PORTER = 1;
        MAX_BUSDRIVER = 1;
        PLANES_PER_DAY = 5;
        Boolean END_OF_DAY = false;
        MAX_BAGS_NUMBER = 2;
        BUS_CAPACITY = 3;

        // Arrays de threads
        TPorter[] TPorter = new TPorter[MAX_PORTER];
        TBusDriver[] TBusDriver = new TBusDriver[MAX_BUSDRIVER];
        TPassenger[] TPassenger = new TPassenger[PLANE_PASSENGERS];

        // INSTANCIAR MONITORES
        MGeneralRepository MGeneralRepository = new MGeneralRepository();
        MArrivalLounge MArrivalLounge = new MArrivalLounge(PLANE_PASSENGERS, MGeneralRepository);
        MArrivalTerminalExit MArrivalTerminalExit = new MArrivalTerminalExit(MGeneralRepository);
        MArrivalTerminalTransferQuay MArrivalTerminalTransferQuay = new MArrivalTerminalTransferQuay(BUS_CAPACITY, MGeneralRepository);
        MBaggageCollectionPoint MBaggageCollectionPoint = new MBaggageCollectionPoint(MGeneralRepository);
        MBaggageReclaimOffice MBaggageReclaimOffice = new MBaggageReclaimOffice(MGeneralRepository);
        MDepartureTerminal MDepartureTerminal = new MDepartureTerminal(MGeneralRepository);
        MDepartureTerminalTransferQuay MDepartureTerminalTransferQuay = new MDepartureTerminalTransferQuay(MGeneralRepository);
        MTemporaryStorageArea MTemporaryStorageArea = new MTemporaryStorageArea(MGeneralRepository);       

        bags = generateBags(PLANE_PASSENGERS, MAX_BAGS_NUMBER);

        // Instanciar threads
        
        for (int i = 0; i < MAX_BUSDRIVER; i++) {
            TBusDriver[i] = new TBusDriver(i, (IArrivalTerminalTransferQuayBusDriver) MArrivalTerminalTransferQuay,
                    (IDepartureTerminalTransferQuayBusDriver) MDepartureTerminalTransferQuay);
            // executa o run
            TBusDriver[i].start();
        }

        for (int i = 0; i < MAX_PORTER; i++) {
            TPorter[i] = new TPorter(i, PLANES_PER_DAY, (IArrivalLoungePorter) MArrivalLounge,
                    (IBaggageCollectionPointPorter) MBaggageCollectionPoint,
                    (ITemporaryStorageAreaPorter) MTemporaryStorageArea);
            // executa o run
            TPorter[i].start();
        }

        for (int i = 0; i < PLANE_PASSENGERS; i++) {
            Random r = new Random();
            boolean t_TRANSIT;
            if (bags[i].size() != 0){
                t_TRANSIT = bags[i].get(0).getTRANSIT();
            }
            else{
                t_TRANSIT = r.nextBoolean();
            }
            TPassenger[i] = new TPassenger(i,t_TRANSIT,
                    (IArrivalLoungePassenger) MArrivalLounge, (IArrivalTerminalExitPassenger) MArrivalTerminalExit,
                    (IArrivalTerminalTransferQuayPassenger) MArrivalTerminalTransferQuay,
                    (IBaggageCollectionPointPassenger) MBaggageCollectionPoint,
                    (IBaggageReclaimOfficePassenger) MBaggageReclaimOffice,
                    (IDepartureTerminalTransferQuayPassenger) MDepartureTerminalTransferQuay,
                    (IDepartureTerminalPassenger) MDepartureTerminal);
            // executa o run
            TPassenger[i].start();
        }

        // Wait for joins
        for (int i = 0; i < TPassenger.length; i++) {
            try {
                TPassenger[i].join();
            } catch (InterruptedException e) {
            }
        }
        for (int i = 0; i < TPorter.length; i++) {
            try {
                TPorter[i].join();
            } catch (InterruptedException e) {
            }
        }
        for (int i = 0; i < TBusDriver.length; i++) {
            try {
                TBusDriver[i].join();
            } catch (InterruptedException e) {
            }
        }
        // MLogger.close();
    }

    private static ArrayList<Bag>[] generateBags(int PLANE_PASSENGERS, int MAX_BAGS_NUMBER) {
        // gerar numero de malas para o passageiro
        Random random = new Random();
        Integer size = random.nextInt(MAX_BAGS_NUMBER + 1);

        ArrayList<Bag>[] bags = new ArrayList[PLANE_PASSENGERS];
        for (int i = 0; i < PLANE_PASSENGERS; i++)
        {
            bags[i] = new ArrayList<Bag>();
        }

        Boolean TRANSIT;
        // atribuir um id unico da mala ao passageiro
        for(int i=0; i<PLANE_PASSENGERS; i++) {
            TRANSIT = random.nextBoolean();
            for (int nbag = 0; nbag < size; nbag++) {
                bags[i].add(new Bag(i * MAX_BAGS_NUMBER + nbag, TRANSIT));
            }
        }
        return bags;
    }
}