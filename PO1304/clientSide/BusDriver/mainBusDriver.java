package clientSide.BusDriver;

import clientSide.Stub.ArrivalTerminalTransferQuayStub;
import clientSide.Stub.DepartureTerminalTransferQuayStub;
import entities.TBusDriver;
import shared_regions_JavaInterfaces.IArrivalTerminalTransferQuayBusDriver;
import shared_regions_JavaInterfaces.IDepartureTerminalTransferQuayBusDriver;

/**
 * Este tipo de dados simula uma solução do lado do cliente do Problema dos
 * Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro. A
 * comunicação baseia-se em passagem de mensagens sobre sockets usando o
 * protocolo TCP.
 */

public class mainBusDriver {

    private static int MAX_BUSDRIVER = 1;

    /**
     * Programa principal.
     */

    public static void main(String[] args) {
        TBusDriver[] TBusDriver = new TBusDriver[MAX_BUSDRIVER]; // array de threads cliente
        ArrivalTerminalTransferQuayStub arrivalTerminalTransferQuayStub; // stub à barbearia
        DepartureTerminalTransferQuayStub departureTerminalTransferQuayStub;
        String serverHostName; // nome do sistema computacional onde está o servidor
        int serverPortNumb; // número do port de escuta do servidor

        arrivalTerminalTransferQuayStub = new ArrivalTerminalTransferQuayStub("localhost", 20030);
        departureTerminalTransferQuayStub = new DepartureTerminalTransferQuayStub("localhost", 20070);
        /* Criação dos threads barbeiro e cliente */

        for (int i = 0; i < MAX_BUSDRIVER; i++) {
            TBusDriver[i] = new TBusDriver(i, (IArrivalTerminalTransferQuayBusDriver) arrivalTerminalTransferQuayStub,
                    (IDepartureTerminalTransferQuayBusDriver) departureTerminalTransferQuayStub);
        }

        /* Arranque da simulação */

        for (int i = 0; i < MAX_BUSDRIVER; i++) {
            TBusDriver[i].start();
        }
        /* Aguardar o fim da simulação */
        for (int i = 0; i < TBusDriver.length; i++) {
            try {
                // MArrivalTerminalTransferQuay.endOfWork();
                TBusDriver[i].join();
            } catch (InterruptedException e) {
            }
        }
        arrivalTerminalTransferQuayStub.setBusDriverOut();
    }
}