package serverSide.GeneralRepository;

import comInf.GeneralRepository.Message;
import comInf.GeneralRepository.MessageException;
import serverSide.ServerCom;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para uma solução do Problema dos Barbeiros
 *   Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático dos
 *   threads barbeiro.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */

public class GeneralRepositoryProxy extends Thread {
    /**
     * Contador de threads lançados
     *
     * @serialField nProxy
     */

    private static int nProxy = 0;

    /**
     * Canal de comunicação
     *
     * @serialField sconi
     */

    private ServerCom sconi;

    /**
     * Interface à barbearia
     *
     * @serialField bShopInter
     */

    private GeneralRepositoryInterface GeneralRepositoryInter;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param sconi      canal de comunicação
     * @param bShopInter interface à barbearia
     */

    public GeneralRepositoryProxy(ServerCom sconi, GeneralRepositoryInterface GeneralRepositoryInter) {
        super("Proxy_" + GeneralRepositoryProxy.getProxyId());

      this.sconi = sconi;
      this.GeneralRepositoryInter = GeneralRepositoryInter;
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
      { outMessage = GeneralRepositoryInter.processAndReply (inMessage);         // processá-lo
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
      { cl = Class.forName ("serverSide.GeneralRepository.GeneralRepositoryProxy");
      }
      catch (ClassNotFoundException e)
      { System.out.println ("O tipo de dados GeneralRepositoryProxy não foi encontrado!");
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
