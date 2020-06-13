package comInf.BaggageCollectionPoint;

import java.io.*;
import java.util.List;
import common_infrastructures.Bag;

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
    /* Passenger */
    /**
     * goCollectABag(Integer id, List<Integer> t)
     */
    public static final int GO_COLLECT_BAG      = 1;

    /* Porter */
    /**
     * warnPassengers()
     */ 
    public static final int WARN_PASS           = 2;

    /**
     * addBag(Bag bag)
     */
    public static final int ADD_BAG             = 3;

    /**
     * Indica que o passageiro recolheu as suas malas
     */
    public static final int COLLECT_BAG_DONE    = 4;

    /**
     * SPorter State AT_THE_PLANES_HOLD
     */
    public static final int STATE_APLH          = 5;

    /**
     * End a plane's cycle
     */
    public static final int NEW_PLANE           = 6;

     /**
     * ACKNOWLEDGE
     */
    public static final int ACK                 = 7;

    /**
     * SHUTDOWN
     */
    public static final int SHUT                = 8;

    /* Campos das mensagens */
    /**
     * Tipo da mensagem
     */

    private int msgType = -1;

    /**
     * Identificador do passageiro
     */
    private int passengerID = -1;

    /**
     * Mala para colocar no Conveyor Belt
     */
    private Bag bag = null;

    /**
     *  Lista de malas do passageiro
     */
    private List<Integer> bags = null;

    /**
     *  Número de malas recolhidas pelo passageiro
     */
    private Integer numberOfBagsRetrieved = -1;

    /**
     * Instanciação de uma mensagem (forma 1).
     *
     * @param type tipo da mensagem
     */

    public Message(int type) 
    {
        msgType = type;
    }

    /**
     * Instanciação de uma mensagem (forma 2).
     *
     * @param type tipo da mensagem
     * @param numberOfBagsRetrieved_t Número de malas recolhidas por um passageiro
     */

    public Message(int type, Integer numberOfBagsRetrieved_t) 
    {
        msgType = type;
        numberOfBagsRetrieved =  numberOfBagsRetrieved_t;
    }

    /**
     * Instanciação de uma mensagem (forma 3).
     *
     * @param type tipo da mensagem
     * @param numberOfBagsRetrieved_t Número de malas recolhidas por um passageiro
     */

    public Message(int type, Bag bag_t) 
    {
        msgType = type;
        bag = bag_t;
    }

    /**
     * Instanciação de uma mensagem (forma 4).
     *
     * @param type tipo da mensagem
     * @param id   passenger's id
     * @param bag_ids lista dos identificadores das malas do passageiro
     */

    public Message(int type, int id, List<Integer> bag_ids)
     {
        msgType = type;
        this.passengerID = id;
        this.bags = bag_ids;
    }

    /**
     * Obtenção do valor do campo tipo da mensagem.
     *
     * @return tipo da mensagem
     */

    public int getType()
     {
        return msgType;
    }

     /**
     * Obtenção do valor com o identificador do passageiro.
     *
     * @return identificador do passageiro
    */
    public int getPassengerID()
     {
        return passengerID;
    }

    /**
     * Obtenção do valor do campo com a lista de malas do passageiro
     *
     * @return lista de malas do passageiro
     */

    public List<Integer> getBagsList() 
    {
        return bags;
    }

    /**
     * Obtenção do valor do campo com a mala para colocar no Conveyor Belt
     *
     * @return Mala para colocar no Conveyor Belt
     */

    public Bag getBag() 
    {
        return bag;
    }

    /**
     * Obtenção do valor do campo com o número de malas recolhidas pelo passageiro
     *
     * @return Número de malas recolhidas pelo passageiro
     */

    public Integer getNumberOfBagsRetrieved() 
    {
        return numberOfBagsRetrieved;
    }

    /**
     * Impressão dos campos internos. Usada para fins de debugging.
     *
     * @return string contendo, em linhas separadas, a concatenação da identificação
     *         de cada campo e valor respectivo
     */

    @Override
    public String toString() {
        return ("Tipo = " + msgType
        + "\nId do Passageiro = " + passengerID
        + "\nIDs das malas do passageiro = " + bags 
        + "\nMala a colocar no Conveyor Belt = " + bag
        + "\nNúmero de malas recolhidas pelo passageiro = " + numberOfBagsRetrieved);
    }
}
