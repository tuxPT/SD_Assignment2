package serverSide.BaggageCollectionPoint;

import common_infrastructures.SPorter;
import serverSide.shared_regions.MBaggageCollectionPoint;
import comInf.BaggageCollectionPoint.MessageException;
import comInf.BaggageCollectionPoint.Message;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class BaggageCollectionPointInterface {
   /**
    * Barbearia (representa o serviço a ser prestado)
    *
    * @serialField bShop
    */

   private MBaggageCollectionPoint BaggageCollectionPoint;

   /**
    * Instanciação do interface à barbearia.
    *
    * @param bShop barbearia
    */

   public BaggageCollectionPointInterface(MBaggageCollectionPoint BaggageCollectionPoint) {
      this.BaggageCollectionPoint = BaggageCollectionPoint;
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
         case Message.GO_COLLECT_BAG:
            if(inMessage.getPassengerID() < 0)
               throw new MessageException("Id do passageiro inválido!", inMessage);
            if(inMessage.getBagsList() == null || inMessage.getBagsList().size() == 0)
               throw new MessageException("O passageiro não tem malas para recolher!", inMessage);
            break;
         case Message.ADD_BAG:
            if(inMessage.getBag() == null)
               throw new MessageException("A mala para colocar no Conveyor Belt é inválida!", inMessage);
            break;
         case Message.WARN_PASS:
            break;
         case Message.NEW_PLANE:
            break;
         case Message.SHUT: // shutdown do servidor
            break;
         default:
            throw new MessageException("Tipo inválido!", inMessage);
      }

      /* seu processamento */

      switch (inMessage.getType())

      {
         case Message.GO_COLLECT_BAG:            
            Integer numberOfBagsRetrieved = BaggageCollectionPoint.goCollectABag(inMessage.getPassengerID(), inMessage.getBagsList());
            outMessage = new Message(Message.COLLECT_BAG_DONE, numberOfBagsRetrieved);
            break;
         case Message.ADD_BAG:
            SPorter state = BaggageCollectionPoint.addBag(inMessage.getBag());
            switch(state){
               case AT_THE_PLANES_HOLD:
               outMessage = new Message(Message.STATE_APLH);
                  break;
               default:
                  break;
            }
            break;
         case Message.WARN_PASS:
            BaggageCollectionPoint.warnPassengers();
            outMessage = new Message(Message.ACK);
            break;
         case Message.NEW_PLANE:
            BaggageCollectionPoint.newPlane();
            outMessage = new Message(Message.ACK);
            break;
         case Message.SHUT: // shutdown do servidor
         mainBaggageCollectionPoint.waitConnection = false;
            (((BaggageCollectionPointProxy) (Thread.currentThread())).getScon()).setTimeout(10);
            outMessage = new Message(Message.ACK); // gerar confirmação
            break;
      }

      return (outMessage);
   }
}
