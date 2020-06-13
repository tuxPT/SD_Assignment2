package clientSide.Passenger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import clientSide.Stub.ArrivalLoungeStub;
import clientSide.Stub.ArrivalTerminalExitStub;
import clientSide.Stub.ArrivalTerminalTransferQuayStub;
import clientSide.Stub.BaggageCollectionPointStub;
import clientSide.Stub.BaggageReclaimOfficeStub;
import clientSide.Stub.DepartureTerminalStub;
import clientSide.Stub.DepartureTerminalTransferQuayStub;
import clientSide.Stub.GeneralRepositoryStub;
import common_infrastructures.Bag;
import entities.TPassenger;
import serverSide.shared_regions.MArrivalLounge;
import serverSide.shared_regions.MArrivalTerminalTransferQuay;
import serverSide.shared_regions.MBaggageCollectionPoint;
import serverSide.shared_regions.MGeneralRepository;
import shared_regions_JavaInterfaces.IArrivalLoungePassenger;
import shared_regions_JavaInterfaces.IArrivalTerminalExitPassenger;
import shared_regions_JavaInterfaces.IArrivalTerminalTransferQuayPassenger;
import shared_regions_JavaInterfaces.IBaggageCollectionPointPassenger;
import shared_regions_JavaInterfaces.IBaggageReclaimOfficePassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalPassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalTransferQuayPassenger;

/**
 * Este tipo de dados simula uma solução do lado do cliente do Problema dos
 * Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro. A
 * comunicação baseia-se em passagem de mensagens sobre sockets usando o
 * protocolo TCP.
 */

public class mainPassenger {
    private static int PLANES_PER_DAY = 5;
    private static int MAX_BAGS_NUMBER = 2;
    private static Random random = new Random();
    private static ArrayList<Bag>[] bags;
    /**
     * Programa principal.
     */
    private static int PLANE_PASSENGERS = 6;
    public static void main(String[] args) {
        int nCustomer = 5; // número de clientes
        int nBarber = 2; // número máximo de barbeiros
        TPassenger[] TPassenger = new TPassenger[PLANE_PASSENGERS];
        IArrivalLoungePassenger ArrivalLoungeStub; // stub à barbearia
        int nIter; // número de iterações do ciclo de vida dos clientes
        String fName; // nome do ficheiro de logging
        String serverHostName; // nome do sistema computacional onde está o servidor
        int serverPortNumb; // número do port de escuta do servidor

        ArrivalLoungeStub mArrivalLoungeStub = new ArrivalLoungeStub("localhost", 20010);
        ArrivalTerminalExitStub mArrivalTerminalExitStub = new ArrivalTerminalExitStub("localhost", 20020);
        ArrivalTerminalTransferQuayStub mArrivalTerminalTransferQuayStub = new ArrivalTerminalTransferQuayStub("localhost", 20030);
        BaggageCollectionPointStub mBaggageCollectionPointStub = new BaggageCollectionPointStub("localhost", 20040);
        BaggageReclaimOfficeStub mBaggageReclaimOfficeStub = new BaggageReclaimOfficeStub("localhost", 20050);
        DepartureTerminalStub mDepartureTerminalStub = new DepartureTerminalStub("localhost", 20060);
        DepartureTerminalTransferQuayStub mDepartureTerminalTransferQuayStub = new DepartureTerminalTransferQuayStub("localhost", 20070);
        GeneralRepositoryStub mGeneralRepositoryStub = new GeneralRepositoryStub("localhost", 20080);

        for (int p = 0; p < PLANES_PER_DAY; p++) {
            mGeneralRepositoryStub.nextFlight();
            // total bags generator
            bags = generateBags(PLANE_PASSENGERS, MAX_BAGS_NUMBER);
            // lost bags generator
            int count = 0;
            int total_bags = 0;
            for (int i = 0; i < bags.length; i++) {
                total_bags += bags[i].size();
                for (int j = 0; j < bags[i].size(); j++) {
                    Integer probability = random.nextInt(100);
                    // 5% of lost bags
                    if (probability > 5) {
                        mArrivalLoungeStub.addBag(bags[i].get(j));
                        count++;
                    }
                }
            }

            mGeneralRepositoryStub.setBags(count);
            mBaggageCollectionPointStub.newPlane();

            for (int i = 0; i < PLANE_PASSENGERS; i++) {
                Random r = new Random();
                boolean t_TRANSIT;
                if (bags[i].size() != 0) {
                    t_TRANSIT = bags[i].get(0).getTRANSIT();
                } else {
                    t_TRANSIT = r.nextBoolean();
                }
                List<Integer> temp = new LinkedList<Integer>();
                for (Bag b : bags[i]) {
                    temp.add(b.getID());
                }
                // instancia
                TPassenger[i] = new TPassenger(i, t_TRANSIT, temp, PLANE_PASSENGERS,
                        (IArrivalLoungePassenger) mArrivalLoungeStub, (IArrivalTerminalExitPassenger) mArrivalTerminalTransferQuayStub,
                        (IArrivalTerminalTransferQuayPassenger) mArrivalTerminalExitStub,
                        (IBaggageCollectionPointPassenger) mBaggageCollectionPointStub,
                        (IBaggageReclaimOfficePassenger) mBaggageReclaimOfficeStub,
                        (IDepartureTerminalTransferQuayPassenger) mDepartureTerminalTransferQuayStub,
                        (IDepartureTerminalPassenger) mDepartureTerminalStub);
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
            mArrivalLoungeStub.waitForPorter();
            mGeneralRepositoryStub.endOfLifePlane();
        }
        mArrivalLoungeStub.endOfWork();
        mArrivalTerminalTransferQuayStub.endOfWork();

        boolean PORTER_ENDED = false; 
        boolean BUSDRIVER_ENDED = false;
        while(!(PORTER_ENDED && BUSDRIVER_ENDED)){
            Thread.sleep(10);
            if(!PORTER_ENDED){
                PORTER_ENDED = ArrivalLoungeStub.hasPorter();
            }
            if(!BUSDRIVER_ENDED){
                BUSDRIVER_ENDED = ArrivalTerminalTransferQuayStub.hasBusDriverEnded();
            }
        }
        mGeneralRepositoryStub.printRepository();
        
        //shutdown dos monitores
        mArrivalLoungeStub.shutdown();
        mArrivalTerminalExitStub.shutdown();
        mArrivalTerminalTransferQuayStub.shutdown();
        mBaggageCollectionPointStub.shutdown();
        mBaggageReclaimOfficeStub.shutdown();
        mDepartureTerminalStub.shutdown();
        mDepartureTerminalTransferQuayStub.shutdown();
        mGeneralRepositoryStub.shutdown();


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
