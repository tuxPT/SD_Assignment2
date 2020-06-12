package serverSide.BaggageReclaimOffice;

import common_infrastructures.SPassenger;
import serverSide.shared_regions.MBaggageReclaimOffice;
import comInf.BaggageReclaimOffice.Message;
import comInf.BaggageReclaimOffice.MessageException;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class BaggageReclaimOfficeInterface {
    /**
     * Barbearia (representa o serviço a ser prestado)
     *
     * @serialField bShop
     */

    private MBaggageReclaimOffice BaggageReclaimOffice;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param bShop barbearia
     */

    public BaggageReclaimOfficeInterface(MBaggageReclaimOffice BaggageReclaimOffice) {
      this.BaggageReclaimOffice = BaggageReclaimOffice;
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

      switch (inMessage.getType()) {
         case Message.ADD_BAG:
            if(inMessage.getPassengerId() < 0)
               throw new MessageException("Id do passageiro inválido!", inMessage);
            if(inMessage.getNumberOfBags() < 0)
               throw new MessageException("O número de malas perdidas é inválido!", inMessage);
            break;
            if ((inMessage.getBarbId() < 0) || (inMessage.getBarbId() >= bShop.getNBarb()))
               throw new MessageException("Id do barbeiro inválido!", inMessage);
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
            SPassenger state = BaggageReclaimOffice.addBag(inMessage.getPassengerId(), inMessage.getNumberOfBags());
            switch(state){
               case EXITING_THE_ARRIVAL_TERMINAL:
                  outMessage = new Message(Message.STATE_EAT);
                  break;
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
