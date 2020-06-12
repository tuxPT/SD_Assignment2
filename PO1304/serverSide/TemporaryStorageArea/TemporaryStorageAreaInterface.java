package serverSide.TemporaryStorageArea;

import comInf.TemporaryStorageArea.Message;
import comInf.TemporaryStorageArea.MessageException;
import common_infrastructures.SPorter;
import serverSide.shared_regions.MTemporaryStorageArea;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class TemporaryStorageAreaInterface {
    /**
     * Barbearia (representa o serviço a ser prestado)
     *
     * @serialField bShop
     */

    private MTemporaryStorageArea TemporaryStorageArea;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param bShop barbearia
     */

    public TemporaryStorageAreaInterface(MTemporaryStorageArea TemporaryStorageArea) {
      this.TemporaryStorageArea = TemporaryStorageArea;
   }

   /**
    * Processamento das mensagens através da execução da tarefa correspondente.
    * Geração de uma mensagem de resposta.
    *
    * @param inMessage mensagem com o pedido
    *
    * @return mensagem de resposta
    *
    * @throws MessageException se a mensagem com o pedido for considerada inválida
    */

   public Message processAndReply(Message inMessage) throws MessageException {
      Message outMessage = null; // mensagem de resposta

      /* validação da mensagem recebida */

      switch (inMessage.getType()) 
      {   
         case Message.ADD_BAG:
            break;
         case Message.SHUT: // shutdown do servidor
            break;
         default:
            throw new MessageException("Tipo inválido!", inMessage);
      }

      /* seu processamento */

      switch (inMessage.getType())
      {
         case Message.ADD_BAG:
            SPorter state = TemporaryStorageArea.addBag();
            switch(state){
               case AT_THE_PLANES_HOLD:
                  outMessage = new Message(Message.STATE_ATPH);
               default:
                  break;
            }
            break;
         case Message.SHUT: // shutdown do servidor
            ServerSleepingBarbers.waitConnection = false;
            (((ClientProxy) (Thread.currentThread())).getScon()).setTimeout(10);
            outMessage = new Message(Message.ACK); // gerar confirmação
            break;
      }

      return (outMessage);
   }
}
