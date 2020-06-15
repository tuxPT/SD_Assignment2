package serverSide.ArrivalTerminalExit;

import comInf.ArrivalTerminalExit.Message;
import comInf.ArrivalTerminalExit.MessageException;
import serverSide.ServerCom;

public class ArrivalTerminalExitProxy extends Thread
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
   *  Interface ao ArrivalTerminalExit
   *
   *    @serialField ArrivalTerminalExitInterface
   */

   private ArrivalTerminalExitInterface ArrivalTerminalExitInter;

  /**
   *  Instanciação do interface à barbearia.
   *
   *    @param sconi canal de comunicação
   *    @param rrivalTerminalExitInter_t interface ao ArrivalTerminalExit
   */

   public ArrivalTerminalExitProxy (ServerCom sconi, ArrivalTerminalExitInterface rrivalTerminalExitInter_t)
   {
      super ("Proxy_" + ArrivalTerminalExitProxy.getProxyId ());

      this.sconi = sconi;
      this.ArrivalTerminalExitInter = rrivalTerminalExitInter_t;
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
      { outMessage = ArrivalTerminalExitInter.processAndReply (inMessage);         // processá-lo
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
      { cl = Class.forName ("serverSide.ArrivalTerminalExit.ArrivalTerminalExitProxy");
      }
      catch (ClassNotFoundException e)
      { System.out.println ("O tipo de dados ArrivalTerminalExitProxy não foi encontrado!");
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
