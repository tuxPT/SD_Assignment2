package serverSide.ArrivalTerminalTransferQuay;

import java.net.SocketTimeoutException;

import clientSide.Stub.GeneralRepositoryStub;
import serverSide.ServerCom;
import serverSide.shared_regions.MArrivalTerminalTransferQuay;
import shared_regions_JavaInterfaces.IGeneralRepository;

// start shared region instance
// start serverInterface, where it maps messages to internal calls
// receive from clients
// set up initial values
// receive from clients
// execute internal calls
public class mainArrivalTerminalTransferQuay {
    /**
     * Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     * @serialField portNumb
     */

    private static int portNumb = 20030;
    public static boolean waitConnection; // sinalização de actividade
    private static int BUS_CAPACITY = 3;

    /**
     * Programa principal.
     */

    public static void main(String[] args) {
        portNumb = Integer.parseInt(args[0]);
        MArrivalTerminalTransferQuay ArrivalTerminalTransferQuay; // barbearia (representa o serviço a ser prestado)
        ArrivalTerminalTransferQuayInterface ArrivalTerminalTransferQuayInter; // interface à barbearia
        ServerCom scon, sconi; // canais de comunicação
        ArrivalTerminalTransferQuayProxy ArrivalTerminalTransferQuayProxy; // thread agente prestador do serviço
        IGeneralRepository MGeneralRepository = new GeneralRepositoryStub("localhost", 20080);

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb); // criação do canal de escuta e sua associação
        scon.start(); // com o endereço público
        ArrivalTerminalTransferQuay = new MArrivalTerminalTransferQuay(BUS_CAPACITY, MGeneralRepository); // activação
                                                                                                          // do serviço
        ArrivalTerminalTransferQuayInter = new ArrivalTerminalTransferQuayInterface(ArrivalTerminalTransferQuay); // activação
                                                                                                                  // do
                                                                                                                  // interface
                                                                                                                  // com
                                                                                                                  // o
                                                                                                                  // serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor está em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try {
                sconi = scon.accept(); // entrada em processo de escuta
                ArrivalTerminalTransferQuayProxy = new ArrivalTerminalTransferQuayProxy(sconi,
                        ArrivalTerminalTransferQuayInter); // lançamento
                // do agente
                // prestador
                // do serviço
                ArrivalTerminalTransferQuayProxy.start();
            } catch (SocketTimeoutException e) {
            }
        scon.end(); // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}
