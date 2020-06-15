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
    private static String AL_HOST, ATE_HOST, ATTQ_HOST, BCP_HOST, BRO_HOST, DT_HOST, DTTQ_HOST, GR_HOST;
    private static int AL_PORT, ATE_PORT, ATTQ_PORT, BCP_PORT, BRO_PORT, DT_PORT, DTTQ_PORT, GR_PORT;

    /**
     * Programa principal.
     */
    private static int PLANE_PASSENGERS = 6;

    public static void main(String[] args) {
        AL_HOST = args[0];
        AL_PORT = Integer.parseInt(args[1]);
        ATE_HOST = args[2];
        ATE_PORT = Integer.parseInt(args[3]);
        BCP_HOST = args[4];
        BCP_PORT = Integer.parseInt(args[5]);
        BRO_HOST = args[6];
        BRO_PORT = Integer.parseInt(args[7]);
        DT_HOST = args[8];
        DT_PORT = Integer.parseInt(args[9]);
        DTTQ_HOST = args[10];
        DTTQ_PORT = Integer.parseInt(args[11]);
        GR_HOST = args[12];
        GR_PORT = Integer.parseInt(args[13]);

        Random random = new Random();
        ArrayList<Bag>[] bags;
        TPassenger[] TPassenger = new TPassenger[PLANE_PASSENGERS];
        String serverHostName; // nome do sistema computacional onde está o servidor
        int serverPortNumb; // número do port de escuta do servidor

        ArrivalLoungeStub mArrivalLoungeStub = new ArrivalLoungeStub(AL_HOST, AL_PORT);
        ArrivalTerminalExitStub mArrivalTerminalExitStub = new ArrivalTerminalExitStub(ATE_HOST, ATE_PORT);
        ArrivalTerminalTransferQuayStub mArrivalTerminalTransferQuayStub = new ArrivalTerminalTransferQuayStub(
                ATTQ_HOST, ATTQ_PORT);
        BaggageCollectionPointStub mBaggageCollectionPointStub = new BaggageCollectionPointStub(BCP_HOST, BCP_PORT);
        BaggageReclaimOfficeStub mBaggageReclaimOfficeStub = new BaggageReclaimOfficeStub(BRO_HOST, BRO_PORT);
        DepartureTerminalStub mDepartureTerminalStub = new DepartureTerminalStub(DT_HOST, DT_PORT);
        DepartureTerminalTransferQuayStub mDepartureTerminalTransferQuayStub = new DepartureTerminalTransferQuayStub(
                DTTQ_HOST, DTTQ_PORT);
        GeneralRepositoryStub mGeneralRepositoryStub = new GeneralRepositoryStub(GR_HOST, GR_PORT);

        for (int p = 0; p < PLANES_PER_DAY; p++) {
            mGeneralRepositoryStub.nextFlight();
            // total bags generator
            bags = generateBags(PLANE_PASSENGERS, MAX_BAGS_NUMBER);
            // lost bags generator
            int count = 0;
            for (int i = 0; i < bags.length; i++) {
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
                        (IArrivalLoungePassenger) mArrivalLoungeStub,
                        (IArrivalTerminalExitPassenger) mArrivalTerminalExitStub,
                        (IArrivalTerminalTransferQuayPassenger) mArrivalTerminalTransferQuayStub,
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
        while (!(PORTER_ENDED && BUSDRIVER_ENDED)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(!PORTER_ENDED){
                PORTER_ENDED = mArrivalLoungeStub.hasPorterEnded();
            }
            if(!BUSDRIVER_ENDED){
                BUSDRIVER_ENDED = mArrivalTerminalTransferQuayStub.hasBusDriverEnded();
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
