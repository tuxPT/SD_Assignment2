package comInf.DepartureTerminal;

import java.io.*;

/**
 *   Este tipo de dados define as mensagens que são trocadas entre os clientes e o servidor numa solução do Problema
 *   dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento
 *   estático dos threads barbeiro.
 *   A comunicação propriamente dita baseia-se na troca de objectos de tipo Message num canal TCP.
 */

public class Message implements Serializable
{
    /**
     *  Chave de serialização
     */

    private static final long serialVersionUID = 1001L;

    /* Tipos das mensagens (passenger) */
    /* Passenger */
    /**
    *  boolean addPassenger(Integer id, Integer curr)
    */
    public static final int ADD_PASS             = 1;

    /**
    * void waitingForLastPassenger()
    */
    public static final int WFLP                 = 2;

    /**
    * Integer getCURRENT_NUMBER_OF_PASSENGERS()
    */
    public static final int GET_NP               = 3;

    /**
    * void lastPassenger()
    */
    public static final int LP                   = 4;

    /**
    * SPassenger prepareNextLeg()
    */
    public static final int PNL                  = 5;

    /**
     * Indicates that the passenger is the last one to attempt to exit the Airport
     */
    public static final int IS_LAST_PASS         = 6;

    /**
     * Indicates that the passenger is not the last one to attempt to exit the Airport
     */
    public static final int IS_NOT_LAST_PASS     = 7;

    /**
    * ACKNOWLEDGE
    */
    public static final int ACK                  = 8;

    /**
    * SPassenger State ENTERING_THE_DEPARTURE_TERMINAL
    */
    public static final int STATE_EDT            = 9;

    /* Campos das mensagens */

    /**
     *  Tipo da mensagem
     */

    private int msgType = -1;

    /**
     *  Identificação do passageiro
     */

    private int passengerID = -1;

     /**
     * número de passageiros no DepartureTerminal
     */
    private int currentNumberOfPassengers = -1;



    private int currentArrival = -1;

    /**
     *  Instanciação de uma mensagem (forma 1).
     *
     *    @param type tipo da mensagem
     */

    public Message (int type)
    {
        msgType = type;
    }

    /**
     * Instanciação de uma mensagem (forma 2).
     *
     * @param type tipo da mensagem
     * @param numberOfPass_t Número de passageiros atualmente no ArrivalTerminalExit
     */

    public Message(int type, int numberOfPass_t) 
    {
        msgType = type;
        currentNumberOfPassengers = numberOfPass_t;
    }

    /**
     * @param type tipo da mensagem
     * @param id identificador do passageiro
     * @param curr número de passageiros atualmente no ArrivalTerminalExit
     */

    public Message (int type, Integer id, Integer curr)
    {
        msgType = type;
        passengerID = id;
        currentArrival = curr;
    }

    /**
     *  Obtenção do valor do campo tipo da mensagem.
     *
     *    @return tipo da mensagem
     */

    public int getType ()
    {
        return msgType;
    }

    /**
     *  Obtenção do valor do campo identificador do passageiro.
     *
     *    @return identificação do passageiro
     */

    public int getPassengerID ()
    {
        return passengerID;
    }

    /**
     *  Obtenção do valor do campo do número de passageiros no ArrivalTerminalExit.
     *
     *    @return número de passageiros no ArrivalTerminalExit
     */

    public int getCurrentArrival()
    {
        return currentArrival;
    }

    /**
     * Obtenção do valor do número de passageiros no DepartureTerminal
     *
     * @return número de passageiros no DepartureTerminal
     */

    public int getCurrentNumberOfPassengers() 
    {
        return currentNumberOfPassengers;
    }

    /**
     *  Impressão dos campos internos.
     *  Usada para fins de debugging.
     *
     *    @return string contendo, em linhas separadas, a concatenação da identificação de cada campo e valor respectivo
     */

    @Override
    public String toString ()
    {
        return ("Tipo = " + msgType
        + "\nId Passageiro = " + passengerID
        + "\nNúmero de Passageiros no ArrivalTerminalExit = " + currentArrival
        + "\nNúmero de passageiros atuais (DepartureTerminal) = " + currentNumberOfPassengers);
    }
}
