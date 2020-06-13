package serverSide.ArrivalTerminalExit;

import java.net.SocketTimeoutException;

import clientSide.Stub.GeneralRepositoryStub;
import serverSide.shared_regions.MArrivalTerminalExit;
import shared_regions_JavaInterfaces.IGeneralRepository;
import serverSide.ServerCom;

/**
 *   Este tipo de dados simula uma solução do lado do servidor do Problema dos Barbeiros Sonolentos que implementa o
 *   modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático dos threads barbeiro.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */
// start shared region instance
        // start serverInterface, where it maps messages to internal calls
            // receive from clients
            // set up initial values
            // receive from clients
            // execute internal calls
public class mainArrivalTerminalExit
{
  /**
   *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
   *
   *    @serialField portNumb
   */

   private static final int portNumb = 22001;
   private static final int PLANE_PASSENGERS = 6;
   public static boolean waitConnection;                              // sinalização de actividade
   

  /**
   *  Programa principal.
   */

   public static void main (String [] args)
   {
      portNumb = Integer.parseInt(args[0]);
      MArrivalTerminalExit ArrivalTerminalExit;                                    // barbearia (representa o serviço a ser prestado)
      ArrivalTerminalExitInterface ArrivalTerminalExitInterface;                      // interface à barbearia
      ServerCom scon, sconi;                               // canais de comunicação
      ArrivalTerminalExitProxy cliProxy;                                // thread agente prestador do serviço
      IGeneralRepository MGeneralRepository = new GeneralRepositoryStub("localhost", port);

     /* estabelecimento do servico */

      scon = new ServerCom (portNumb);                     // criação do canal de escuta e sua associação
      scon.start ();                                       // com o endereço público
      ArrivalTerminalExit = new MArrivalTerminalExit(PLANE_PASSENGERS, MGeneralRepository);                           // activação do serviço
      ArrivalTerminalExitInterface = new ArrivalTerminalExitInterface (ArrivalTerminalExit);        // activação do interface com o serviço
      System.out.println ("O serviço foi estabelecido!");
      System.out.println ("O servidor está em escuta.");

     /* processamento de pedidos */

      waitConnection = true;
      while (waitConnection)
        try
        { sconi = scon.accept ();                          // entrada em processo de escuta
          cliProxy = new ArrivalTerminalExitProxy (sconi, ArrivalTerminalExitInterface);  // lançamento do agente prestador do serviço
          cliProxy.start ();
        }
        catch (SocketTimeoutException e)
        {
        }
      scon.end ();                                         // terminação de operações
      System.out.println ("O servidor foi desactivado.");
   }
}
