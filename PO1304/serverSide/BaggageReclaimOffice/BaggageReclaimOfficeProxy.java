package serverSide.BaggageReclaimOffice;

import comInf.MessageException;
import genclass.GenericIO;
import serverSide.ServerCom;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para uma solução do Problema dos Barbeiros
 *   Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático dos
 *   threads barbeiro.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */

public class ClientProxy extends Thread
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
   *  Interface à barbearia
   *
   *    @serialField bShopInter
   */

   private BarberShopInterface bShopInter;

  /**
   *  Instanciação do interface à barbearia.
   *
   *    @param sconi canal de comunicação
   *    @param bShopInter interface à barbearia
   */

   public ClientProxy (ServerCom sconi, BarberShopInterface bShopInter)
   {
      super ("Proxy_" + ClientProxy.getProxyId ());

      this.sconi = sconi;
      this.bShopInter = bShopInter;
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
      { outMessage = bShopInter.processAndReply (inMessage);         // processá-lo
      }
      catch (MessageException e)
      { GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
        GenericIO.writelnString (e.getMessageVal ().toString ());
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
      { cl = Class.forName ("serverSide.ClientProxy");
      }
      catch (ClassNotFoundException e)
      { GenericIO.writelnString ("O tipo de dados ClientProxy não foi encontrado!");
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
