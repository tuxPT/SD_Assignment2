package serverSide.DepartureTerminal;

import comInf.DepartureTerminal.Message;
import common_infrastructures.SPassenger;
import serverSide.shared_regions.MDepartureTerminal;
import comInf.DepartureTerminal.MessageException;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class DepartureTerminalInterface {
    /**
     * Barbearia (representa o serviço a ser prestado)
     *
     * @serialField DepartureTerminal
     */

    private MDepartureTerminal DepartureTerminal;

    /**
     * Instanciação do interface ao DepartureTerminal.
     *
     * @param DepartureTerminal DepartureTerminal
     */

    public DepartureTerminalInterface(MDepartureTerminal DepartureTerminal) {
        this.DepartureTerminal = DepartureTerminal;
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
            case Message.ADD_PASS:
                if (inMessage.getPassengerID() < 0)
                    throw new MessageException("ID do passageiro inválido!", inMessage);
                if (inMessage.getCurrentArrival() < 0)
                    throw new MessageException("O número de passageiros no ArrivalTerminalExit é inválido!", inMessage);
                break;
            case Message.GET_NP:
                break;
            case Message.PNL:
                break;
            case Message.LP:
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
                boolean isLastPass = DepartureTerminal.addPassenger(inMessage.getPassengerID(),
                        inMessage.getCurrentArrival());
                if (isLastPass) {
                    outMessage = new Message(Message.IS_LAST_PASS);
                } else {
                    outMessage = new Message(Message.IS_NOT_LAST_PASS);
                }
                break;
            case Message.GET_NP:
                Integer numberOfPass = DepartureTerminal.getCURRENT_NUMBER_OF_PASSENGERS();
                if (numberOfPass < 0)
                    throw new MessageException("O número de passageiros recebido (DepartureTerminal) é inválido!",
                            inMessage);
                outMessage = new Message(Message.ACK, numberOfPass);
                break;
            case Message.PNL:
                SPassenger state = DepartureTerminal.prepareNextLeg();
                switch (state) {
                    case ENTERING_THE_DEPARTURE_TERMINAL:
                        outMessage = new Message(Message.STATE_EDT);
                        break;
                    default:
                        break;
                }
                break;
            case Message.LP:
                DepartureTerminal.lastPassenger();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT: // shutdown do servidor
                mainDepartureTerminal.waitConnection = false;
                (((DepartureTerminalProxy) (Thread.currentThread())).getScon()).setTimeout(10);
                outMessage = new Message(Message.ACK); // gerar confirmação
                break;
        }

        return (outMessage);
    }
}
