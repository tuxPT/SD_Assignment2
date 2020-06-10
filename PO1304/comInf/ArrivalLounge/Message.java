package comInf.ArrivalLounge;

import java.io.*;

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
     * whatShouldIDo(pthread_number, bags.size(), TRANSIT)
     */
    public static final int WSD = 1;

    /* Porter */
    /**
     *  takeARest()
     */
    public static final int TAKE_REST = 2;

    /**
     * tryToCollectABag()
     */
    public static final int TRY_COLLECT = 3;

    /**
     * noMoreBagsToCollect()
     */
    public static final int NO_MORE_BAGS = 4;

    /**
     * carryItToAppropriateStore(Bag bag)
     */
    public static final int CARRY_TO_APP_STORE = 5;

    /**
     * SPassenger State AT_THE_ARRIVAL_TRANSFER_TERMINAL
     */ 
    public static final int STATE_ATT = 6;

    /**
     * SPassenger State AT_THE_LUGGAGE_COLLECTION_POINT
     */
    public static final int STATE_LCP = 7;

    /**
     * SPassenger State EXITING_THE_ARRIVAL_TERMINAL
     */ 
    public static final int STATE_EAT = 8;

    /**
     * SPorter State WAITING_FOR_A_PLANE_TO_LAND
     */
    public static final int STATE_WPTL = 9;

    /**
     * SPorter State AT_THE_STOREROOM
     */
    public static final int STATE_ASTR = 10;

    /**
     * SPorter State AT_THE_LUGGAGE_BELT_CONVEYOR
     */
    public static final int STATE_ALCB = 11;

    /* Campos das mensagens */

    /**
     * Tipo da mensagem
     */

    private int msgType = -1;

    /**
     * Identificação do passageiro
     */

    private int passengerID = -1;

    /**
     * Número de malas
     */

    private int bags = -1;

    /**
     * Identificação do tipo de mala
     */

    private boolean transit = false;

    /**
     * Mala
     */

    private Bag bag = null;

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
   *  Instanciação de uma mensagem (forma 2).
   *
   *    @param type tipo da mensagem
   *    @param bag mala
   */

   public Message (int type, Bag bag)
   {
        msgType = type;
        this.bag = bag;
   }

    /**
     * Instanciação de uma mensagem (forma 3).
     *
     * @param type  tipo da mensagem
     * @param passengerID identificador do passageiro
     * @param bags número de malas
     * @param transit tipo de mala (trânsito ou destino final)
     */

    public Message(int type, Integer id, Integer t_bags, boolean t_TRANSIT) {
        msgType = type;
        this.passengerID = id;
        this.bags = t_bags;
        this.transit = t_TRANSIT;
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
     * Obtenção do valor do campo identificador do passageiro.
     *
     * @return identificador do passageiro
     */

    public int getPassengerID() 
    {
        return passengerID;
    }

    /**
     * Obtenção do valor do campo com o número de malas.
     *
     * @return número de malas
     */

    public int getBags() 
    {
        return bags;
    }

    /**
     * Obtenção do valor do campo com o tipo de mala (trânsito ou destino final).
     *
     * @return tipo de mala (trânsito ou destino final)
     */

    public boolean getTransit() 
    {
        return transit;
    }

    /**
     * Obtenção do valor do campo com a mala
     *
     * @return mala
     */

    public Bag getBag() 
    {
        return bag;
    }

    /**
     * Impressão dos campos internos. Usada para fins de debugging.
     *
     * @return string contendo, em linhas separadas, a concatenação da identificação
     *         de cada campo e valor respectivo
     */

    @Override
    public String toString() {
        return ("Tipo = " + msgType + "\nID do passageiro = " + bags + "\nNumero de malas do passageiro = " + bags
                + "\nPassageiro de transito = " + transit + "\nMala do passageiro = " + bag);
    }
}
