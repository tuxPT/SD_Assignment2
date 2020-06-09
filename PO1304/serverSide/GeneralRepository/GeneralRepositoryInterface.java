package serverSide.GeneralRepository;

import comInf.ArrivalLounge.Message;
import common_infrastructures.SPassenger;
import serverSide.shared_regions.MArrivalLounge;
import comInf.MessageException;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class GeneralRepositoryInterface {
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

    public GeneralRepositoryInterface(MArrivalLounge ArrivalLounge) {
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

      switch (inMessage.getType()) {
         case Message.SETNFIC:
            if ((inMessage.getFName() == null) || (inMessage.getFName().equals("")))
               throw new MessageException("Nome do ficheiro inexistente!", inMessage);
            break;
         case Message.REQCUTH:
            if ((inMessage.getCustId() < 0) || (inMessage.getCustId() >= bShop.getNCust()))
               throw new MessageException("Id do cliente inválido!", inMessage);
            break;
         case Message.ENDOP:
         case Message.GOTOSLP:
         case Message.CALLCUST:
            if ((inMessage.getBarbId() < 0) || (inMessage.getBarbId() >= bShop.getNBarb()))
               throw new MessageException("Id do barbeiro inválido!", inMessage);
            break;
         case Message.GETPAY:
            if ((inMessage.getBarbId() < 0) || (inMessage.getBarbId() >= bShop.getNBarb()))
               throw new MessageException("Id do barbeiro inválido!", inMessage);
            if ((inMessage.getCustId() < 0) || (inMessage.getCustId() >= bShop.getNCust()))
               throw new MessageException("Id do cliente inválido!", inMessage);
            break;
         case Message.SHUT: // shutdown do servidor
            break;
         default:
            throw new MessageException("Tipo inválido!", inMessage);
      }

      /* seu processamento */

      switch (inMessage.getType())

      {
         case Message.WSD: // inicializar ficheiro de logging
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
               default: break;
            }
            break;

         case Message.TAKE_REST:
            if (bShop.goCutHair(inMessage.getCustId())) // o cliente quer cortar o cabelo
               outMessage = new Message(Message.CUTHDONE); // gerar resposta positiva
            else
               outMessage = new Message(Message.BSHOPF); // gerar resposta negativa
            break;
         case Message.TRY_COLLECT:
            if (bShop.goToSleep(inMessage.getBarbId())) // o barbeiro vai dormir
               outMessage = new Message(Message.END); // gerar resposta positiva
            else
               outMessage = new Message(Message.CONT); // gerar resposta negativa
            break;
         case Message.NO_MORE_BAGS:
            int custID = bShop.callCustomer(inMessage.getBarbId()); // chamar cliente
            outMessage = new Message(Message.CUSTID, custID); // enviar id do cliente
            break;
         case Message.CARRY_TO_APP_STORE: // receber pagamento
            bShop.getPayment(inMessage.getBarbId(), inMessage.getCustId());
            outMessage = new Message(Message.ACK); // gerar confirmação
            break;
         case Message.ENDOP: // fim de operações do barbeiro
            bShop.endOperation(inMessage.getBarbId());
            outMessage = new Message(Message.ACK); // gerar confirmação
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
