package serverSide.DepartureTerminalTransferQuay;

import comInf.DepartureTerminalTransferQuay.Message;
import comInf.DepartureTerminalTransferQuay.MessageException;
import serverSide.ServerCom;

public class DepartureTerminalTransferQuayProxy extends Thread
{
  /**
   *  Contador de threads lançados
   *
   *    @serialField nProxy
   */

   private static int nProxy = 0;

  /**
   *  Canal de comunicação
   *
   *    @serialField sconi
   */

   private ServerCom sconi;

  /**
   *  Interface ao DepartureTerminalTransferQuay
   *
   *    @serialField DepartureTerminalTransferQuayInterface
   */

   private DepartureTerminalTransferQuayInterface DepartureTerminalTransferQuayInter;

  /**
   *  Instanciação do interface ao DepartureTerminalTransferQuay.
   *
   *    @param sconi canal de comunicação
   *    @param DepartureTerminalTransferQuayInter_t interface ao DepartureTerminalTransferQuay
   */

   public DepartureTerminalTransferQuayProxy (ServerCom sconi, DepartureTerminalTransferQuayInterface DepartureTerminalTransferQuayInter_t)
   {
      super ("Proxy_" + DepartureTerminalTransferQuayProxy.getProxyId ());

      this.sconi = sconi;
      this.DepartureTerminalTransferQuayInter = DepartureTerminalTransferQuayInter_t;
   }

  /**
   *  Ciclo de vida do thread agente prestador de serviço.
   */

   @Override
   public void run ()
   {
      Message inMessage = null,                                      // mensagem de entrada
              outMessage = null;                      // mensagem de saída

      inMessage = (Message) sconi.readObject ();                     // ler pedido do cliente
      try
      { outMessage = DepartureTerminalTransferQuayInter.processAndReply (inMessage);         // processá-lo
      }
      catch (MessageException e)
      { System.out.println ("Thread " + getName () + ": " + e.getMessage () + "!");
        System.out.println (e.getMessageVal ().toString ());
        System.exit (1);
      }
      sconi.writeObject (outMessage);                                // enviar resposta ao cliente
      sconi.close ();                                                // fechar canal de comunicação
   }

  /**
   *  Geração do identificador da instanciação.
   *
   *    @return identificador da instanciação
   */

   private static int getProxyId ()
   {
      Class<?> cl = null;                                  // representação do tipo de dados ClientProxy na máquina
                                                           //   virtual de Java
      int proxyId;                                         // identificador da instanciação

      try
      { cl = Class.forName ("serverSide.DepartureTerminalTransferQuay.DepartureTerminalTransferQuayProxy");
      }
      catch (ClassNotFoundException e)
      { System.out.println ("O tipo de dados DepartureTerminalTransferQuayProxy não foi encontrado!");
        e.printStackTrace ();
        System.exit (1);
      }

      synchronized (cl)
      { proxyId = nProxy;
        nProxy += 1;
      }

      return proxyId;
   }

  /**
   *  Obtenção do canal de comunicação.
   *
   *    @return canal de comunicação
   */

   public ServerCom getScon ()
   {
      return sconi;
   }
}
