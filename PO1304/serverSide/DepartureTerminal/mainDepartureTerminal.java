package serverSide.DepartureTerminal;

import genclass.GenericIO;
import java.net.SocketTimeoutException;

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
public class mainDepartureTerminal
{
  /**
   *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
   *
   *    @serialField portNumb
   */

   private static final int portNumb = 22001;
   public static boolean waitConnection;                              // sinalização de actividade

  /**
   *  Programa principal.
   */

   public static void main (String [] args)
   {
      ArrivalLounge ArrivalLounge;                                    // barbearia (representa o serviço a ser prestado)
      ArrivalLoungeInterface ArrivalLoungeInterface;                      // interface à barbearia
      ServerCom scon, sconi;                               // canais de comunicação
      ClientProxy cliProxy;                                // thread agente prestador do serviço

     /* estabelecimento do servico */

      scon = new ServerCom (portNumb);                     // criação do canal de escuta e sua associação
      scon.start ();                                       // com o endereço público
      bShop = new BarberShop ();                           // activação do serviço
      bShopInter = new BarberShopInterface (bShop);        // activação do interface com o serviço
      GenericIO.writelnString ("O serviço foi estabelecido!");
      GenericIO.writelnString ("O servidor esta em escuta.");

     /* processamento de pedidos */

      waitConnection = true;
      while (waitConnection)
        try
        { sconi = scon.accept ();                          // entrada em processo de escuta
          cliProxy = new ClientProxy (sconi, bShopInter);  // lançamento do agente prestador do serviço
          cliProxy.start ();
        }
        catch (SocketTimeoutException e)
        {
        }
      scon.end ();                                         // terminação de operações
      GenericIO.writelnString ("O servidor foi desactivado.");
   }
}
