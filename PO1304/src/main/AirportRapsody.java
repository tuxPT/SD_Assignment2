package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import common_infrastructures.Bag;
import entities.TBusDriver;
import entities.TPassenger;
import entities.TPorter;
import serverSide.shared_regions.MArrivalLounge;
import serverSide.shared_regions.MArrivalTerminalExit;
import serverSide.shared_regions.MArrivalTerminalTransferQuay;
import serverSide.shared_regions.MBaggageCollectionPoint;
import serverSide.shared_regions.MBaggageReclaimOffice;
import serverSide.shared_regions.MDepartureTerminal;
import serverSide.shared_regions.MDepartureTerminalTransferQuay;
import serverSide.shared_regions.MGeneralRepository;
import serverSide.shared_regions.MTemporaryStorageArea;
import shared_regions_JavaInterfaces.IArrivalLoungePassenger;
import shared_regions_JavaInterfaces.IArrivalLoungePorter;
import shared_regions_JavaInterfaces.IArrivalTerminalExitPassenger;
import shared_regions_JavaInterfaces.IArrivalTerminalTransferQuayBusDriver;
import shared_regions_JavaInterfaces.IArrivalTerminalTransferQuayPassenger;
import shared_regions_JavaInterfaces.IBaggageCollectionPointPassenger;
import shared_regions_JavaInterfaces.IBaggageCollectionPointPorter;
import shared_regions_JavaInterfaces.IBaggageReclaimOfficePassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalPassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalTransferQuayBusDriver;
import shared_regions_JavaInterfaces.IDepartureTerminalTransferQuayPassenger;
import shared_regions_JavaInterfaces.ITemporaryStorageAreaPorter;

public class AirportRapsody {

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        ArrayList<Bag>[] bags;
        Integer PLANE_PASSENGERS, MAX_PORTER, MAX_BUSDRIVER, PLANES_PER_DAY, MAX_BAGS_NUMBER,BUS_CAPACITY;
        PLANE_PASSENGERS = 6;
        MAX_PORTER = 1;
        MAX_BUSDRIVER = 1;
        PLANES_PER_DAY = 5;        
        MAX_BAGS_NUMBER = 2;
        BUS_CAPACITY = 3;

        // Arrays de threads
        TPorter[] TPorter = new TPorter[MAX_PORTER];
        TBusDriver[] TBusDriver = new TBusDriver[MAX_BUSDRIVER];
        TPassenger[] TPassenger = new TPassenger[PLANE_PASSENGERS];

        // INSTANCIAR MONITORES
        MGeneralRepository MGeneralRepository = new MGeneralRepository(PLANE_PASSENGERS, BUS_CAPACITY);
        MArrivalLounge MArrivalLounge = new MArrivalLounge(PLANE_PASSENGERS, MGeneralRepository);
        MArrivalTerminalExit MArrivalTerminalExit = new MArrivalTerminalExit(PLANE_PASSENGERS, MGeneralRepository);
        MArrivalTerminalTransferQuay MArrivalTerminalTransferQuay = new MArrivalTerminalTransferQuay(BUS_CAPACITY, MGeneralRepository);
        MBaggageCollectionPoint MBaggageCollectionPoint = new MBaggageCollectionPoint(MGeneralRepository);
        MBaggageReclaimOffice MBaggageReclaimOffice = new MBaggageReclaimOffice(MGeneralRepository);
        MDepartureTerminal MDepartureTerminal = new MDepartureTerminal(PLANE_PASSENGERS, MGeneralRepository);
        MDepartureTerminalTransferQuay MDepartureTerminalTransferQuay = new MDepartureTerminalTransferQuay(MGeneralRepository);
        MTemporaryStorageArea MTemporaryStorageArea = new MTemporaryStorageArea(MGeneralRepository);       

        // Instanciar threads
        
        for (int i = 0; i < MAX_BUSDRIVER; i++) {
            TBusDriver[i] = new TBusDriver(i, (IArrivalTerminalTransferQuayBusDriver) MArrivalTerminalTransferQuay,
                    (IDepartureTerminalTransferQuayBusDriver) MDepartureTerminalTransferQuay);
            // executa o run
            TBusDriver[i].start();
        }

        for (int i = 0; i < MAX_PORTER; i++) {
            TPorter[i] = new TPorter(i, (IArrivalLoungePorter) MArrivalLounge,
                    (IBaggageCollectionPointPorter) MBaggageCollectionPoint,
                    (ITemporaryStorageAreaPorter) MTemporaryStorageArea);
            // executa o run
            TPorter[i].start();
        }
        for(int p=0; p<PLANES_PER_DAY; p++) {
            MGeneralRepository.nextFlight();
            //total bags generator
            bags = generateBags(PLANE_PASSENGERS, MAX_BAGS_NUMBER);
            //lost bags generator
            int count = 0;
            int total_bags = 0;
            for(int i=0; i<bags.length; i++){
                total_bags += bags[i].size();
                for(int j=0; j<bags[i].size(); j++){
                    Integer probability = random.nextInt(100);
                    //5% of lost bags
                    if(probability > 5){
                        MArrivalLounge.addBag(bags[i].get(j));
                        count++;
                    }
                }
            }

            MGeneralRepository.setBags(count);
            MBaggageCollectionPoint.newPlane();

            for (int i = 0; i < PLANE_PASSENGERS; i++) {
                Random r = new Random();
                boolean t_TRANSIT;
                if (bags[i].size() != 0){
                    t_TRANSIT = bags[i].get(0).getTRANSIT();
                }
                else{
                    t_TRANSIT = r.nextBoolean();
                }
                List<Integer> temp = new LinkedList<Integer>();
                for(Bag b: bags[i]){
                    temp.add(b.getID());
                }
                //instancia
                TPassenger[i] = new TPassenger(i,t_TRANSIT, temp, PLANE_PASSENGERS,
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
            MArrivalLounge.waitForPorter();
            MGeneralRepository.endOfLifePlane();
        }

        for (int i = 0; i < TPorter.length; i++) {
            try {
                MArrivalLounge.endOfWork();
                TPorter[i].join();

            } catch (InterruptedException e) {
            }
        }

        for (int i = 0; i < TBusDriver.length; i++) {
            try {
                MArrivalTerminalTransferQuay.endOfWork();
                TBusDriver[i].join();
            } catch (InterruptedException e) {
            }
        }
        //print end of day
        MGeneralRepository.printRepository();
    }

    private static ArrayList<Bag>[] generateBags(int PLANE_PASSENGERS, int MAX_BAGS_NUMBER) {
        // gerar numero de malas para o passageiro
        Random random = new Random();
        Integer size;

        ArrayList<Bag>[] bags = new ArrayList[PLANE_PASSENGERS];
        for (int i = 0; i < PLANE_PASSENGERS; i++)
        {
            bags[i] = new ArrayList<Bag>();
        }

        Boolean TRANSIT;
        // atribuir um id unico da mala ao passageiro
        for(int i=0; i<PLANE_PASSENGERS; i++) {
            TRANSIT = random.nextBoolean();
            size = random.nextInt(MAX_BAGS_NUMBER + 1);
            for (int nbag = 0; nbag < size; nbag++) {
                bags[i].add(new Bag(i * MAX_BAGS_NUMBER + nbag, TRANSIT));
            }
        }
        return bags;
    }
}
