package serverSide.DepartureTerminalTransferQuay;

import java.net.SocketTimeoutException;

import clientSide.Stub.GeneralRepositoryStub;
import serverSide.shared_regions.MDepartureTerminalTransferQuay;
import shared_regions_JavaInterfaces.IGeneralRepository;
import serverSide.ServerCom;

// start shared region instance
        // start serverInterface, where it maps messages to internal calls
            // receive from clients
            // set up initial values
            // receive from clients
            // execute internal calls
public class mainDepartureTerminalTransferQuay
{
  /**
   *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
   *
   *    @serialField portNumb
   */

   private static int portNumb = 20070;
   public static boolean waitConnection;                              // sinalização de actividade

  /**
   *  Programa principal.
   */

   public static void main (String [] args)
   {
      portNumb = Integer.parseInt(args[0]);
      MDepartureTerminalTransferQuay DepartureTerminalTransferQuay;                                    // barbearia (representa o serviço a ser prestado)
      DepartureTerminalTransferQuayInterface DepartureTerminalTransferQuayInter;                      // interface à barbearia
      ServerCom scon, sconi;                               // canais de comunicação
      DepartureTerminalTransferQuayProxy cliProxy;                                // thread agente prestador do serviço

     /* estabelecimento do servico */

      scon = new ServerCom (portNumb);                     // criação do canal de escuta e sua associação
      scon.start ();                                       // com o endereço público
      IGeneralRepository MGeneralRepository = (IGeneralRepository) new GeneralRepositoryStub("localhost", 20080);
      DepartureTerminalTransferQuay = new MDepartureTerminalTransferQuay(MGeneralRepository);         // activação do serviço
      DepartureTerminalTransferQuayInter = new DepartureTerminalTransferQuayInterface (DepartureTerminalTransferQuay);        // activação do interface com o serviço
      System.out.println ("O serviço foi estabelecido!");
      System.out.println ("O servidor está em escuta.");

     /* processamento de pedidos */

      waitConnection = true;
      while (waitConnection)
        try
        { sconi = scon.accept ();                          // entrada em processo de escuta
          cliProxy = new DepartureTerminalTransferQuayProxy (sconi, DepartureTerminalTransferQuayInter);  // lançamento do agente prestador do serviço
          cliProxy.start ();
        }
        catch (SocketTimeoutException e)
        {
        }
      scon.end ();                                         // terminação de operações
      System.out.println ("O servidor foi desactivado.");
   }
}
