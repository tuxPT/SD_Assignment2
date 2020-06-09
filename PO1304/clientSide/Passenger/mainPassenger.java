package clientSide.Passenger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import clientSide.Stub.ArrivalLoungeStub;
import common_infrastructures.Bag;
import entities.TPassenger;
import genclass.GenericIO;
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
    /**
     * Programa principal.
     */

    public static void main(String[] args) {
        int nCustomer = 5; // número de clientes
        int nBarber = 2; // número máximo de barbeiros
        TPassenger[] TPassenger = new TPassenger[PLANE_PASSENGERS];
        IArrivalLoungePassenger ArrivalLoungeStub; // stub à barbearia
        int nIter; // número de iterações do ciclo de vida dos clientes
        String fName; // nome do ficheiro de logging
        String serverHostName; // nome do sistema computacional onde está o servidor
        int serverPortNumb; // número do port de escuta do servidor

        /* Obtenção dos parâmetros do problema */
        GenericIO.writelnString("\n" + "      Problema dos Barbeiros Sonolentos\n");
        GenericIO.writeString("Numero de iterações? ");
        nIter = GenericIO.readlnInt();
        GenericIO.writeString("Nome do ficheiro de logging? ");
        fName = GenericIO.readlnString();
        GenericIO.writeString("Nome do sistema computacional onde está o servidor? ");
        serverHostName = GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do servidor? ");
        serverPortNumb = GenericIO.readlnInt();
        ArrivalLoungeStub = new ArrivalLoungeStub(serverHostName, serverPortNumb);

        // INSTANCIAR MONITORES

        /* Comunicação ao servidor dos parâmetros do problema */

        bShopStub.probPar(fName, nIter);

        for (int p = 0; p < PLANES_PER_DAY; p++) {
            MGeneralRepository.nextFlight();
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
        MArrivalLounge.endOfWork();
        MArrivalTerminalTransferQuay.endOfWork();

        //shutdown dos monitores
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
