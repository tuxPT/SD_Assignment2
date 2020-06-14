package serverSide.GeneralRepository;

import java.util.logging.Logger;

import comInf.GeneralRepository.Message;
import comInf.GeneralRepository.MessageException;
import common_infrastructures.SPassenger;
import serverSide.shared_regions.MGeneralRepository;
import shared_regions_JavaInterfaces.IGeneralRepository;

/**
 * Este tipo de dados define o interface à barbearia numa solução do Problema
 * dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class GeneralRepositoryInterface {

    private static final Logger LOGGER = Logger.getLogger( GeneralRepositoryInterface.class.getName() );

    /**
     * Barbearia (representa o serviço a ser prestado)
     *
     * @serialField bShop
     */

    private IGeneralRepository GeneralRepository;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param bShop barbearia
     */

    public GeneralRepositoryInterface(IGeneralRepository generalRepository2) {
        this.GeneralRepository = generalRepository2;
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
            case Message.UPDATE_BUSDRIVER:
                /*if(inMessage.getSBusDriver() == null)
                    throw new MessageException("O estado do BusDriver é inválido!", inMessage);
                if(inMessage.getPrint() != true && inMessage.getPrint() != false)
                    throw new MessageException("A indicação para imprimir o repositório é inválida!", inMessage);
                */break;
            case Message.UPDATE_PASSENGER:/*
                if(inMessage.getsPassengerState() == null)
                    throw new MessageException("O estado do Passenger é inválido!", inMessage);*/
                /*if(inMessage.getsPassengerID() < 0)
                    throw new MessageException("O ID do passageiro é inválido!", inMessage);*/
                    /*
                if(inMessage.getsAddWaitingQueue() == null)
                    throw new MessageException("addWaitingQueue é inválido!", inMessage);
                if(inMessage.getsAddBusSeats() == null)
                    throw new MessageException("addBusSeats é inválido!", inMessage);
                if(inMessage.getsStartBags() < 0)
                    throw new MessageException("startBags é inválido!", inMessage);
                if(inMessage.getsCollectBags() == null)
                    throw new MessageException("collectBags é inválido!", inMessage);
                if(inMessage.getsTransit() == null)
                    throw new MessageException("Transit é inválido!", inMessage);
                    */break;
            case Message.UPDATE_PORTER:
                break;
            case Message.NEXT_FLIGHT:
                break;
            case Message.END_OF_LIFE_PLANE:
                break;
            case Message.PRINT:
                break;
            case Message.SET_BAGS:
                if(inMessage.getNumberOfBags() < 0)
                    throw new MessageException("O número de malas para colocar no GeneralRepository é inválido!", inMessage);
                break;
            case Message.SHUT: // shutdown do servidor
                break;
            default:
                throw new MessageException("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType())

        {
            case Message.UPDATE_BUSDRIVER:
                GeneralRepository.updateBusDriver(inMessage.getSBusDriver(), inMessage.getPrint());
                outMessage = new Message(Message.ACK);
                break;
            case Message.UPDATE_PASSENGER:
                GeneralRepository.updatePassenger(inMessage.getsPassengerState(), inMessage.getsPassengerID(),
                inMessage.getsAddWaitingQueue(),inMessage.getsAddBusSeats(),
                inMessage.getsStartBags(),inMessage.getsCollectBags(),inMessage.getsTransit());
                outMessage = new Message(Message.ACK);
                break;
            case Message.UPDATE_PORTER:
                GeneralRepository.updatePorter(inMessage.getsPorterState(), inMessage.getsBN(),
                inMessage.getsBN(),inMessage.getsSR(),inMessage.getPrint());
                outMessage = new Message(Message.ACK);
                break;
            case Message.NEXT_FLIGHT:
                GeneralRepository.nextFlight();
                outMessage = new Message(Message.ACK);
                break;
            case Message.END_OF_LIFE_PLANE:
                GeneralRepository.endOfLifePlane();
                outMessage = new Message(Message.ACK);
                break;
            case Message.PRINT:
                GeneralRepository.printRepository();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SET_BAGS:
                GeneralRepository.setBags(inMessage.getNumberOfBags());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT: // shutdown do servidor
                mainGeneralRepository.waitConnection = false;
                (((GeneralRepositoryProxy) (Thread.currentThread())).getScon()).setTimeout(10);
                outMessage = new Message(Message.ACK); // gerar confirmação
                break;
        }
        LOGGER.info(inMessage.toString());

        return (outMessage);
    }
}
