package serverSide.ArrivalTerminalTransferQuay;

import comInf.ArrivalTerminalTransferQuay.Message;
import common_infrastructures.SPassenger;
import serverSide.shared_regions.MArrivalTerminalTransferQuay;
import comInf.ArrivalTerminalTransferQuay.MessageException;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class ArrivalTerminalTransferQuayInterface {
    /**
     * Barbearia (representa o serviço a ser prestado)
     *
     * @serialField bShop
     */

    private MArrivalTerminalTransferQuay ArrivalTerminalTransferQuay;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param bShop barbearia
     */

    public ArrivalTerminalTransferQuayInterface(MArrivalTerminalTransferQuay ArrivalTerminalTransferQuay) {
      this.ArrivalTerminalTransferQuay = ArrivalTerminalTransferQuay;
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
         case Message.ANN_BUS_BOARD:
            break;
         case Message.ENTER_BUS:
            if(inMessage.getPassengerID() < 0)
               throw new MessageException("ID do passageiro inválido!", inMessage);            
            break;
         case Message.GO2DEP_TERMINAL:
            break;
         case Message.END_OF_WORK:
            break;
         case Message.SHUT: // shutdown do servidor
            break;
         default:
            throw new MessageException("Tipo inválido!", inMessage);
      }

      /* seu processamento */

      switch (inMessage.getType())
      {
         case Message.ANN_BUS_BOARD:
            boolean no_more_work = ArrivalTerminalTransferQuay.announcingBusBoarding();
            if(no_more_work){
               outMessage = new Message(Message.NO_MORE_WORK);
            }
            else{
               outMessage = new Message(Message.BUS_LEAVING);
            }
            break;
         case Message.ENTER_BUS:
            SPassenger state = ArrivalTerminalTransferQuay.enterTheBus(inMessage.getPassengerID());
            switch(state){
               case TERMINAL_TRANSFER:
                  outMessage = new Message(Message.STATE_TRT);
                  break;
               default:
                  break;
            }
            break;
         case Message.GO2DEP_TERMINAL:
            Integer numberOfPassOnBoard = ArrivalTerminalTransferQuay.goToDepartureTerminal();
            outMessage = new Message(Message.ACK,numberOfPassOnBoard);
            break;
         case Message.END_OF_WORK:
            ArrivalTerminalTransferQuay.endOfWork();
            outMessage = new Message(Message.ACK);
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
