package serverSide.BaggageReclaimOffice;

import java.net.SocketTimeoutException;

import clientSide.Stub.GeneralRepositoryStub;
import serverSide.shared_regions.MBaggageReclaimOffice;
import shared_regions_JavaInterfaces.IGeneralRepository;
import serverSide.ServerCom;

// start shared region instance
// start serverInterface, where it maps messages to internal calls
// receive from clients
// set up initial values
// receive from clients
// execute internal calls
public class mainBaggageReclaimOffice {
    /**
     * Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     * @serialField portNumb
     */

    private static int portNumb = 20050;
    public static boolean waitConnection; // sinalização de actividade

    /**
     * Programa principal.
     */

    public static void main(String[] args) {
        //portNumb = Integer.parseInt(args[0]);
        MBaggageReclaimOffice BaggageReclaimOffice; // barbearia (representa o serviço a ser prestado)
        BaggageReclaimOfficeInterface MBaggageReclaimOfficeInter; // interface à barbearia
        ServerCom scon, sconi; // canais de comunicação
        BaggageReclaimOfficeProxy BaggageReclaimOfficeProxy; // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb); // criação do canal de escuta e sua associação
        scon.start(); // com o endereço público
        IGeneralRepository MGeneralRepository = (IGeneralRepository) new GeneralRepositoryStub("localhost", 20080);
        BaggageReclaimOffice = new MBaggageReclaimOffice(MGeneralRepository); // activação do serviço
        MBaggageReclaimOfficeInter = new BaggageReclaimOfficeInterface(BaggageReclaimOffice); // activação do interface
                                                                                              // com o serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor está em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try {
                sconi = scon.accept(); // entrada em processo de escuta
                BaggageReclaimOfficeProxy = new BaggageReclaimOfficeProxy(sconi, MBaggageReclaimOfficeInter); // lançamento                                                                                    // serviço
                BaggageReclaimOfficeProxy.start();
            } catch (SocketTimeoutException e) {
            }
        scon.end(); // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}
