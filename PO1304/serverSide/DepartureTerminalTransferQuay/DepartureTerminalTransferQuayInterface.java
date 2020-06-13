package serverSide.DepartureTerminalTransferQuay;

import comInf.DepartureTerminalTransferQuay.Message;
import common_infrastructures.SBusDriver;
import common_infrastructures.SPassenger;
import serverSide.shared_regions.MDepartureTerminalTransferQuay;
import comInf.DepartureTerminalTransferQuay.MessageException;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class DepartureTerminalTransferQuayInterface {
    /**
     * Barbearia (representa o serviço a ser prestado)
     *
     * @serialField bShop
     */

    private MDepartureTerminalTransferQuay DepartureTerminalTransferQuay;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param bShop barbearia
     */

    public DepartureTerminalTransferQuayInterface(MDepartureTerminalTransferQuay DepartureTerminalTransferQuay) {
      this.DepartureTerminalTransferQuay = DepartureTerminalTransferQuay;
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
         case Message.PBLPF:
            if(inMessage.getNumberOfPassengersOnBus() < 0)
               throw new MessageException("O número de passageiros a bordo do autocarro é inválido!", inMessage);
            break;
         case Message.GTAT:
            break;
         case Message.LTB:
            if(inMessage.getPassengerID() < 0)
               throw new MessageException("O ID do passageiro é inválido!", inMessage);
            break;
         case Message.SHUT: // shutdown do servidor
            break;
         default:
            throw new MessageException("Tipo inválido!", inMessage);
      }

      /* seu processamento */

      switch (inMessage.getType())
      {
         case Message.PBLPF:{
            SBusDriver state = DepartureTerminalTransferQuay.parkTheBusAndLetPassOff(inMessage.getNumberOfPassengersOnBus());
            switch(state){
               case DRIVING_BACKWARD:
                  outMessage = new Message(Message.STATE_DB);
                  break;
               default:
                  break;
            }
            break;}
         case Message.GTAT:{
         SBusDriver state = DepartureTerminalTransferQuay.goToArrivalTerminal();
         switch(state){
            case PARKING_AT_THE_ARRIVAL_TERMINAL:
               outMessage = new Message(Message.STATE_PKAT);
               break;
            default:
               break;
         }
            break;}
         case Message.LTB:
            SPassenger state = DepartureTerminalTransferQuay.leaveTheBus(inMessage.getPassengerID());
            switch(state){
               case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
                  outMessage = new Message(Message.STATE_DTT);
                  break;
               default:
                  break;
            }
            break;
         case Message.SHUT: // shutdown do servidor
            mainDepartureTerminalTransferQuay.waitConnection = false;
            (((DepartureTerminalTransferQuayProxy) (Thread.currentThread())).getScon()).setTimeout(10);
            outMessage = new Message(Message.ACK); // gerar confirmação
            break;
      }

      return (outMessage);
   }
}
