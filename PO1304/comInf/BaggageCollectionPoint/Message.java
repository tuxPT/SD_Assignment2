package comInf.BaggageCollectionPoint;

import java.io.*;
import java.util.List;

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
    public static final int GO_COLLECT_BAG = 1;

    /* Porter */
    /**
     * warnPassengers()
     */ 
    public static final int WARN_PASS = 2;

    /**
     * addBag(Bag bag)
     */
    public static final int ADD_BAG = 3;

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
     * Identificador do dono da mala para colocar no Conveyor Belt
     */
    private Integer bagID = -1;

    /**
     *  Lista de malas do passageiro
     */
    private List<Integer> bags = null;

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
     * Instanciação de uma mensagem (forma 1).
     *
     * @param type tipo da mensagem
     * @param bag_id Identificador do dono da mala para colocar no Conveyor Belt
     */

    public Message(int type, Integer bag_id) 
    {
        msgType = type;
        bagID = bag_id;
    }

    /**
     * Instanciação de uma mensagem (forma 2).
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
     * Obtenção do valor do campo identificador do cliente.
     *
     * @return identificação do cliente
     */

    public int getPassengerID() 
    {
        return passengerID;
    }

    /**
     * Obtenção do valor do campo identificador do cliente.
     *
     * @return Identificador do dono da mala para colocar no Conveyor Belt
     */

    public int getBagID() 
    {
        return bagID;
    }

    /**
     * Impressão dos campos internos. Usada para fins de debugging.
     *
     * @return string contendo, em linhas separadas, a concatenação da identificação
     *         de cada campo e valor respectivo
     */

    @Override
    public String toString() {
        return ("Tipo = " + msgType + "\nId do Passageiro = " + passengerID + "\nNumero de malas = " + bags + "\nIdentificador da mala a colocar no Conveyor Belt = " + bagID);
    }
}
