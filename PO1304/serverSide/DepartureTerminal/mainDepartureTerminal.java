package serverSide.DepartureTerminal;

import java.net.SocketTimeoutException;

import clientSide.Stub.GeneralRepositoryStub;
import serverSide.shared_regions.MDepartureTerminal;
import shared_regions_JavaInterfaces.IGeneralRepository;
import serverSide.ServerCom;

// start shared region instance
// start serverInterface, where it maps messages to internal calls
// receive from clients
// set up initial values
// receive from clients
// execute internal calls
public class mainDepartureTerminal {
    /**
     * Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     * @serialField portNumb
     */

    private static int portNumb = 20060;
    public static boolean waitConnection; // sinalização de actividade
    private static int PLANE_PASSENGERS = 6;

    /**
     * Programa principal.
     */

    public static void main(String[] args) {
        portNumb = Integer.parseInt(args[0]);
        MDepartureTerminal DepartureTerminal; // barbearia (representa o serviço a ser prestado)
        DepartureTerminalInterface DepartureTerminalInter; // interface à barbearia
        ServerCom scon, sconi; // canais de comunicação
        DepartureTerminalProxy cliProxy; // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb); // criação do canal de escuta e sua associação
        scon.start(); // com o endereço público
        IGeneralRepository MGeneralRepository = (IGeneralRepository) new GeneralRepositoryStub("localhost", 20080);
        DepartureTerminal = new MDepartureTerminal(PLANE_PASSENGERS, MGeneralRepository); // activação do serviço
        DepartureTerminalInter = new DepartureTerminalInterface(DepartureTerminal); // activação do interface com o
                                                                                    // serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor está em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try {
                sconi = scon.accept(); // entrada em processo de escuta
                cliProxy = new DepartureTerminalProxy(sconi, DepartureTerminalInter); // lançamento do agente prestador
                                                                                      // do serviço
                cliProxy.start();
            } catch (SocketTimeoutException e) {
            }
        scon.end(); // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}
