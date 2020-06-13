package serverSide.GeneralRepository;

import java.net.SocketTimeoutException;

import serverSide.ServerCom;
import serverSide.shared_regions.MGeneralRepository;
import shared_regions_JavaInterfaces.IGeneralRepository;

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
public class mainGeneralRepository
{
  /**
   *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
   *
   *    @serialField portNumb
   */

   private static int portNumb = 20080;
   public static boolean waitConnection;                              // sinalização de actividade
   private static int PLANE_PASSENGERS = 6;
   private static int BUS_CAPACITY = 3;
  /**
   *  Programa principal.
   */

   public static void main (String [] args)
   {
      IGeneralRepository GeneralRepository;                                    // barbearia (representa o serviço a ser prestado)
      GeneralRepositoryInterface GeneralRepositoryInterface;                      // interface à barbearia
      ServerCom scon, sconi;                               // canais de comunicação
      GeneralRepositoryProxy GeneralRepositoryProxy;                                // thread agente prestador do serviço

     /* estabelecimento do servico */

      scon = new ServerCom (portNumb);                     // criação do canal de escuta e sua associação
      scon.start ();                                       // com o endereço público
      GeneralRepository = new MGeneralRepository(PLANE_PASSENGERS, BUS_CAPACITY);                           // activação do serviço
      GeneralRepositoryInterface = new GeneralRepositoryInterface(GeneralRepository);      // activação do interface com o serviço
      System.out.println ("O serviço foi estabelecido!");
      System.out.println ("O servidor esta em escuta.");

     /* processamento de pedidos */

      waitConnection = true;
      while (waitConnection)
        try
        { sconi = scon.accept ();                          // entrada em processo de escuta
          GeneralRepositoryProxy = new GeneralRepositoryProxy(sconi, GeneralRepositoryInterface);  // lançamento do agente prestador do serviço
          GeneralRepositoryProxy.start ();
        }
        catch (SocketTimeoutException e)
        {
        }
      scon.end ();                                         // terminação de operações
      System.out.println ("O servidor foi desactivado.");
   }
}
