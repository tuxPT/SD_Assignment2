package comInf.GeneralRepository;

import java.io.*;
import java.util.logging.Logger;

import common_infrastructures.SBusDriver;
import common_infrastructures.SPassenger;
import common_infrastructures.SPorter;

/**
 * Este tipo de dados define as mensagens que são trocadas entre os clientes e o
 * servidor numa solução do Problema dos Barbeiros Sonolentos que implementa o
 * modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento
 * estático dos threads barbeiro. A comunicação propriamente dita baseia-se na
 * troca de objectos de tipo Message num canal TCP.
 */

public class Message implements Serializable {
    /**
     * Chave de serialização
     */

    private static final long serialVersionUID = 1001L;

    /* Tipos das mensagens */
    /**
     * updateBusDriver(SBusDriver Stat, boolean print)
     */
    public static final int UPDATE_BUSDRIVER     = 1;

    /**
     * updatePassenger(SPassenger Stat, Integer id, Boolean addWaitingQueue, Boolean addBusSeats,
            Integer startBags, Boolean collectBags, Boolean transit)
     */
    public static final int UPDATE_PASSENGER     = 2;

    /**
     * updatePorter(SPorter Stat, Integer BN, Integer CB, Integer SR, boolean print)
     */
    public static final int UPDATE_PORTER        = 3;

    /**
     * nextFlight()
     */
    public static final int NEXT_FLIGHT          = 4;

    /**
     * endOfLifePlane()
     */
    public static final int END_OF_LIFE_PLANE    = 5;

     /**
     * printRepository()
     */
    public static final int PRINT                = 6;

    /**
    * setBags(int count);
    */
    public static final int SET_BAGS             = 7;

    /**
     * Shutdown do servidor (operação pedida pelo cliente)
     */

    public static final int SHUT                 = 8;
 
    /**
     * ACKNOWLEDGE
     */

    public static final int ACK                  = 9;

    /* Campos das mensagens */

    /**
     * Tipo da mensagem
     */

    private int msgType = -1;

    /* BUSDRIVER */
    /**
     * Identificação do estado do BusDriver
     */
    private SBusDriver sBusDriverState = null;

    /* PASSENGER */
    /**
     * Identificação do estado do Passenger
     */
    private SPassenger sPassengerState = null;

    /* PASSENGER */

    /**
     * Número de malas
     */
    private int numberOfBags = -1;

    /**
     * ID do passageiro
     */
    private Integer passengerID = null;

    /**
     * addWaitingQueue
     */
    private Boolean addWaitingQueue = null;

    /**
     * addBusSeats
     */
    private Boolean addBusSeats = null;

    /**
     * Número de malas inicial do passageiro
     */
    private Integer startBags = null;

    /**
     * Número de malas inicial do passageiro
     */
    private Boolean collectBags = null;

    /**
     * Tipo de passageiro
     */
    private Boolean transit = null;

    /* PORTER */
    /**
     * Identificação do estado do Porter
     */
    private SPorter sPorterState = null;

    /**
     * Número de malas total
     */
    private Integer BN = null;

    /**
     * Número de malas no Conveyor Belt
     */
    private Integer CB = null;

    /**
     * Número de malas no StoreRoom
     */
    private Integer SR = null;

    /**
     * Indicação a dizer se é para imprimir o Repositório
     */
    private boolean print;

    /**
     * Instanciação de uma mensagem (forma 1).
     *
     * @param type tipo da mensagem
     */

    public Message(int type) {
        msgType = type;
    }

    /**
     * Instanciação de uma mensagem (forma 2).
     *
     * @param type  tipo da mensagem
     * @param state identificação do estado do BusDriver
     * @param print indicação a dizer se é para imprimir o Repositório
     */

    public Message(int type, SBusDriver state, boolean print) {
        msgType = type;
        sBusDriverState = state;
        this.print = print;
    }

    /**
     * Instanciação de uma mensagem (forma 3).
     *
     * @param type   tipo da mensagem
     * @param state estado do passageiro
     * @param id  identificação do passageiro
     * @param addWaitingQueue_t addWaitingQueue
     * @param addBusSeats_t addBusSeats
     * @param startBags_t Número de malas inicial do passageiro
     * @param collectBags_t collectBags
     * @param transit_t tipo de passageiro
     */

    public Message(int type, SPassenger state, Integer id, Boolean addWaitingQueue_t,
    Boolean addBusSeats_t, Integer startBags_t, Boolean collectBags_t, Boolean transit_t) {
        msgType = type;
        sPassengerState = state;
        passengerID = id;
        addWaitingQueue = addWaitingQueue_t;
        addBusSeats = addBusSeats_t;
        startBags = startBags_t;
        collectBags = collectBags_t;
        transit = transit_t;
    }

    /**
     * Instanciação de uma mensagem (forma 4).
     *
     * @param type  tipo da mensagem
     * @param name  nome do ficheiro de logging
     * @param nIter número de iterações do ciclo de vida dos clientes
     */

    public Message(int type, SPorter state, Integer BN, Integer CB, Integer SR, boolean print) {
        msgType = type;
        this.sPorterState = state;
        this.BN = BN;
        this.CB = CB;
        this.SR = SR;
        this.print = print;
    }

     /**
     * Instanciação de uma mensagem (forma 5).
     *
     * @param type  tipo da mensagem
     * @param count Número de malas para colocar no GeneralRepository
     */

    public Message(int type, int count) {
        msgType = type;
        numberOfBags = count;
    }

    /**
     * Obtenção do valor do campo tipo da mensagem.
     *
     * @return tipo da mensagem
     */

    public int getType() {
        return msgType;
    }

    /**
     * Obtenção do valor com o estado do BusDriver
     *
     * @return estado do BusDriver
     */

    public SBusDriver getSBusDriver() {
        return sBusDriverState;
    }

    /**
     * Obtenção do valor com a indicação se é para imprimir o repositório
     *
     * @return print (true/false)
     */

    public boolean getPrint() {
        return print;
    }

     /**
     * Obtenção do estado do passageiro
     *
     * @return estado do passageiro
     */
     public SPassenger getsPassengerState() {
        return sPassengerState;
    }

    /**
     * Obtenção do ID do passageiro
     *
     * @return ID do passageiro
     */
     public Integer getsPassengerID() {
        return passengerID;
    }

    /**
     * Obtenção do bool addWaitingQueue
     *
     * @return true/false
     */
     public Boolean getsAddWaitingQueue() {
        return addWaitingQueue;
    }

    /**
     * Obtenção do bool addWaitingQueue
     *
     * @return true/false
     */
     public Boolean getsAddBusSeats() {
        return addBusSeats;
    }

    /**
     * Obtenção do número inicial de malas do passageiro
     *
     * @return número inicial de malas do passageiro
     */
     public Integer getsStartBags() {
        return startBags;
    }

    /**
     * Obtenção do bool collectBags
     *
     * @return true/false
     */
     public Boolean getsCollectBags() {
        return collectBags;
    }

    /**
     * Obtenção do bool transit
     *
     * @return true/false
     */
     public Boolean getsTransit() {
        return transit;
    }

    /**
     * Obtenção do estado do Porter
     *
     * @return estado do Porter
     */
     public SPorter getsPorterState() {
        return sPorterState;
    }

    /**
     * Obtenção número total de malas
     *
     * @return número total de malas
     */
     public Integer getsBN() {
        return BN;
    }

    /**
     * Obtenção número de malas no Conveyor Belt
     *
     * @return número de malas no Conveyor Belt
     */
     public Integer getsCB() {
        return CB;
    }

     /**
     * Obtenção número de malas no StoreRoom
     *
     * @return número de malas no StoreRoom
     */
     public Integer getsSR() {
        return SR;
    }

     /**
     * Obtenção número de malas no GeneralRepository
     *
     * @return número de malas no GeneralRepository
     */
    public Integer getNumberOfBags() {
        return numberOfBags;
    }

    /**
     * Impressão dos campos internos. Usada para fins de debugging.
     *
     * @return string contendo, em linhas separadas, a concatenação da identificação
     *         de cada campo e valor respectivo
     */

    @Override
    public String toString() {
        if (msgType == UPDATE_BUSDRIVER){
            return ("Tipo = " + msgType
                    + "\nBusDriver's state = " + sBusDriverState
                    + "\nTo print? = " + print);
        }
        else if(msgType == UPDATE_PASSENGER){
            return ("Tipo = " + msgType
                    + "\nPassenger's state = " + sPassengerState
                    + "\nPassenger's ID = " + passengerID
                    + "\naddWaitingQueue = " + addWaitingQueue
                    + "\naddBusSeats = " + addBusSeats
                    + "\nstartBags = " + startBags
                    + "\ncollectBags = " + collectBags
                    + "\ntransit = " + transit);
        }
        else if(msgType == UPDATE_PORTER){
            return ("Tipo = " + msgType
                    + "\nPorter's state = " + sPorterState
                    + "\nBN = " + BN
                    + "\nCB = " + CB
                    + "\nSR = " + SR
                    + "\nTo print? = " + print);
        }
        else if(msgType == SET_BAGS){
            return ("Tipo = " + msgType
                    + "\nNúmero de malas = " + numberOfBags);
        }
        else{
            return ("TIpo = " + msgType);
        }
    }
}
