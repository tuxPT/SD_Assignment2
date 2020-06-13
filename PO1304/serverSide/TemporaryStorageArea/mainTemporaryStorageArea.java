package serverSide.TemporaryStorageArea;

import java.net.SocketTimeoutException;
import serverSide.shared_regions.MTemporaryStorageArea;
import serverSide.ServerCom;

// start shared region instance
        // start serverInterface, where it maps messages to internal calls
            // receive from clients
            // set up initial values
            // receive from clients
            // execute internal calls
public class mainTemporaryStorageArea
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
      portNumb = Integer.parseInt(args[0]);
      MTemporaryStorageArea TemporaryStorageArea;                                    // barbearia (representa o serviço a ser prestado)
      TemporaryStorageAreaInterface TemporaryStorageAreaInter;                      // interface à barbearia
      ServerCom scon, sconi;                               // canais de comunicação
      TemporaryStorageAreaProxy cliProxy;                                // thread agente prestador do serviço

     /* estabelecimento do servico */

      scon = new ServerCom (portNumb);                     // criação do canal de escuta e sua associação
      scon.start ();                                       // com o endereço público
      TemporaryStorageArea = new MTemporaryStorageArea(MGeneralRepository);         // activação do serviço
      TemporaryStorageAreaInter = new TemporaryStorageAreaInterface (TemporaryStorageArea);        // activação do interface com o serviço
      System.out.println ("O serviço foi estabelecido!");
      System.out.println ("O servidor está em escuta.");

     /* processamento de pedidos */

      waitConnection = true;
      while (waitConnection)
        try
        { sconi = scon.accept ();                          // entrada em processo de escuta
          cliProxy = new TemporaryStorageAreaProxy (sconi, TemporaryStorageAreaInter);  // lançamento do agente prestador do serviço
          cliProxy.start ();
        }
        catch (SocketTimeoutException e)
        {
        }
      scon.end ();                                         // terminação de operações
      System.out.println ("O servidor foi desactivado.");
   }
}
