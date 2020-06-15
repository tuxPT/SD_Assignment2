package serverSide.ArrivalTerminalExit;

import serverSide.shared_regions.MArrivalTerminalExit;
import comInf.ArrivalTerminalExit.Message;
import comInf.ArrivalTerminalExit.MessageException;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class ArrivalTerminalExitInterface {
    /**
     * Barbearia (representa o serviço a ser prestado)
     *
     * @serialField bShop
     */

    private MArrivalTerminalExit ArrivalTerminalExit;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param bShop barbearia
     */

    public ArrivalTerminalExitInterface(MArrivalTerminalExit ArrivalTerminalExit) {
      this.ArrivalTerminalExit = ArrivalTerminalExit;
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
         case Message.ADD_PASS:
            if(inMessage.getPassengerID() < 0)
               throw new MessageException("ID do passageiro inválido!", inMessage);
            if(inMessage.getPassengersDeparture() < 0)
               throw new MessageException("O número de passageiros no DepartureTerminal é inválido!", inMessage);
            break;     
         case Message.WAITING_FOR_LAST_PASS:
            break;
         case Message.NUMBER_PASS:
            break;
         case Message.LAST_PASS:
            break;
         case Message.SHUT: // shutdown do servidor
            break;
         default:
            throw new MessageException("Tipo inválido!", inMessage);
      }

      /* seu processamento */

      switch (inMessage.getType())
      {
         case Message.ADD_PASS:
            boolean lastPassenger = ArrivalTerminalExit.addPassenger(inMessage.getPassengerID(), inMessage.getPassengersDeparture());
            if(lastPassenger){
               outMessage = new Message(Message.IS_LAST_PASS);
            }
            else{
               outMessage = new Message(Message.IS_NOT_LAST_PASS);
            }
            break;
         case Message.WAITING_FOR_LAST_PASS:
            ArrivalTerminalExit.waitingForLastPassenger();
            outMessage = new Message(Message.ACK);
            break;
         case Message.NUMBER_PASS:
            Integer numberOfPass = ArrivalTerminalExit.getCURRENT_NUMBER_OF_PASSENGERS();
            if(numberOfPass < 0)
               throw new MessageException("O número de passageiros recebido (ArrivalTerminalExit) é inválido!", inMessage);
            outMessage = new Message(Message.ACK, numberOfPass);
            break;
         case Message.LAST_PASS:
            ArrivalTerminalExit.lastPassenger();
            outMessage = new Message(Message.ACK);
            break;
         case Message.SHUT: // shutdown do servidor
            mainArrivalTerminalExit.waitConnection = false;
            (((ArrivalTerminalExitProxy) (Thread.currentThread())).getScon()).setTimeout(1);
            outMessage = new Message(Message.ACK); // gerar confirmação
            break;
      }

      return outMessage;
   }
}
