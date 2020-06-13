package serverSide.DepartureTerminal;

import comInf.DepartureTerminal.Message;
import comInf.DepartureTerminal.MessageException;
import serverSide.ServerCom;

public class DepartureTerminalProxy extends Thread
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
   *  Interface ao DepartureTerminal
   *
   *    @serialField DepartureTerminalnterface
   */

   private DepartureTerminalInterface DepartureTerminalnter;

  /**
   *  Instanciação do interface ao DepartureTerminal.
   *
   *    @param sconi canal de comunicação
   *    @param DepartureTerminalnterfaceInter_t interface ao DepartureTerminal
   */

   public DepartureTerminalProxy (ServerCom sconi, DepartureTerminalInterface DepartureTerminalnterfaceInter_t)
   {
      super ("Proxy_" + DepartureTerminalProxy.getProxyId ());

      this.sconi = sconi;
      this.DepartureTerminalnter = DepartureTerminalnterfaceInter_t;
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
      { outMessage = DepartureTerminalnter.processAndReply (inMessage);         // processá-lo
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
      { cl = Class.forName ("serverSide.DepartureTerminal.DepartureTerminalProxy");
      }
      catch (ClassNotFoundException e)
      { System.out.println ("O tipo de dados DepartureTerminalProxy não foi encontrado!");
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
