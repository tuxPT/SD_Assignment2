package serverSide.ArrivalLounge;

import java.net.SocketTimeoutException;

import clientSide.Stub.GeneralRepositoryStub;
import serverSide.ServerCom;
import serverSide.shared_regions.MArrivalLounge;
import shared_regions_JavaInterfaces.IGeneralRepository;

/**
 * Este tipo de dados simula uma solução do lado do servidor do Problema dos
 * Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro. A
 * comunicação baseia-se em passagem de mensagens sobre sockets usando o
 * protocolo TCP.
 */
// start shared region instance
// start serverInterface, where it maps messages to internal calls
// receive from clients
// set up initial values
// receive from clients
// execute internal calls
public class mainArrivalLounge {
    /**
     * Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     * @serialField portNumb
     */

    private static int portNumb = 20010;
    private static int PLANE_PASSENGERS = 6;
    private static String GR_HOST;
    private static int GR_PORT;

    public static boolean waitConnection; // sinalização de actividade

    /**
     * Programa principal.
     */

    public static void main(String[] args) {
        portNumb = Integer.parseInt(args[0]);
        GR_HOST = args[1];
        GR_PORT = Integer.parseInt(args[2]);
        MArrivalLounge ArrivalLounge; // barbearia (representa o serviço a ser prestado)
        ArrivalLoungeInterface ArrivalLoungeInterface; // interface à barbearia
        ServerCom scon, sconi; // canais de comunicação
        ArrivalLoungeProxy ArrivalLoungeProxy; // thread agente prestador do serviço
        IGeneralRepository MGeneralRepository = (IGeneralRepository) new GeneralRepositoryStub(GR_HOST, GR_PORT);

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb); // criação do canal de escuta e sua associação
        scon.start(); // com o endereço público
        ArrivalLounge = new MArrivalLounge(PLANE_PASSENGERS, MGeneralRepository); // activação do serviço
        ArrivalLoungeInterface = new ArrivalLoungeInterface(ArrivalLounge); // activação do interface com o serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor está em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try {
                sconi = scon.accept(); // entrada em processo de escuta
                ArrivalLoungeProxy = new ArrivalLoungeProxy(sconi, ArrivalLoungeInterface); // lançamento do agente prestador do
                                                                                  // serviço
                ArrivalLoungeProxy.start();
            } catch (SocketTimeoutException e) {
            }
        scon.end(); // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}
