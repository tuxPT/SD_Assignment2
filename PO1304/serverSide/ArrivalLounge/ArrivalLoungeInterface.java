package serverSide.ArrivalLounge;

import common_infrastructures.Bag;
import comInf.ArrivalLounge.MessageException;
import comInf.ArrivalLounge.Message;
import common_infrastructures.SPassenger;
import common_infrastructures.SPorter;
import serverSide.shared_regions.MArrivalLounge;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class ArrivalLoungeInterface {
   /**
    * Barbearia (representa o serviço a ser prestado)
    *
    * @serialField bShop
    */

   private MArrivalLounge ArrivalLounge;

   /**
    * Instanciação do interface à barbearia.
    *
    * @param bShop barbearia
    */

   public ArrivalLoungeInterface(MArrivalLounge ArrivalLounge) {
      this.ArrivalLounge = ArrivalLounge;
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
         case Message.TRY_COLLECT:
            break;         
         case Message.TAKE_REST:
            break;
         case Message.CARRY_TO_APP_STORE:
            if(inMessage.getBag() == null)
               throw new MessageException("A mala não existe!", inMessage);
            break;
         case Message.WSD:
            if (inMessage.getPassengerID() < 0)
               throw new MessageException("Id de passageiro inválido!", inMessage);
            if (inMessage.getBags() < 0)
               throw new MessageException("Número de malas inválidas!", inMessage);
            if (inMessage.getTransit() != false && inMessage.getTransit() != true)
               throw new MessageException("Tipo de passageiro inválido!", inMessage);
            break;
         case Message.NO_MORE_BAGS:
            break;
         case Message.ADD_BAG:
            if(inMessage.getBag() == null)
               throw new MessageException("A mala não existe!", inMessage);
            break;
         case Message.END_OF_WORK:
            break;
         case Message.WAIT_FOR_PORTER:
            break;
         case Message.SHUT: // shutdown do servidor
            break;
         default:
            throw new MessageException("Tipo inválido!", inMessage);
      }

      /* seu processamento */

      switch (inMessage.getType())
      {
         case Message.TRY_COLLECT:
            Bag bag = ArrivalLounge.tryToCollectABag();
            if (bag == null){
               outMessage = new Message(Message.NO_BAG);
            }
            else{
               outMessage = new Message(Message.COLLECT_DONE);
            }
            break;
         case Message.TAKE_REST:
            boolean no_more_work = ArrivalLounge.takeARest();
            if (no_more_work){
               outMessage = new Message(Message.NO_MORE_WORK);
            }
            else{
               outMessage = new Message(Message.PLANE_ARRIVED);
            }
            break;
         case Message.CARRY_TO_APP_STORE:{ // receber pagamento
            SPorter state = ArrivalLounge.carryItToAppropriateStore(inMessage.getBag());
            switch(state){
               case AT_THE_STOREROOM:
                  outMessage = new Message(Message.STATE_ASTR);
                  break;
               case AT_THE_LUGGAGE_BELT_CONVEYOR:
                  outMessage = new Message(Message.STATE_ALCB);
                  break;
               default:
                  break;
            }
            break;}
         case Message.WSD:{
            SPassenger state = ArrivalLounge.whatShouldIDo(inMessage.getPassengerID(), inMessage.getBags(),
                  inMessage.getTransit());
            switch (state) {
               case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
                  outMessage = new Message(Message.STATE_ATT);
                  break;
               case AT_THE_LUGGAGE_COLLECTION_POINT:
                  outMessage = new Message(Message.STATE_LCP);
                  break;
               case EXITING_THE_ARRIVAL_TERMINAL:
                  outMessage = new Message(Message.STATE_EAT);
                  break;
               default: 
                  break;
            }
            break;}       
         case Message.NO_MORE_BAGS:
            SPorter state = ArrivalLounge.noMoreBagsToCollect();
            switch(state){
               case WAITING_FOR_A_PLANE_TO_LAND:
                  outMessage = new Message(Message.STATE_WPTL);
                  break;
               default:
                  break;
            }
            break;
         case Message.ADD_BAG:
            ArrivalLounge.addBag(inMessage.getBag());
            outMessage = new Message(Message.ACK);
            break;
         case Message.END_OF_WORK:
            ArrivalLounge.endOfWork();
            outMessage = new Message(Message.ACK);
            break;
         case Message.WAIT_FOR_PORTER:
            ArrivalLounge.waitForPorter();
            outMessage = new Message(Message.ACK);
            break;
         case Message.SHUT: // shutdown do servidor
            ServerSleepingBarbers.waitConnection = false;
            (((ClientProxy) (Thread.currentThread())).getScon()).setTimeout(10);
            outMessage = new Message(Message.ACK); // gerar confirmação
            break;
      }

      return outMessage;
   }
}
