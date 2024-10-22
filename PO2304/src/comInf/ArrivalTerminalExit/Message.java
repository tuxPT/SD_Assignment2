package comInf.ArrivalTerminalExit;

import java.io.*;

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
    // Passengers
    /**
     * addPassenger(Integer id, Integer curr)
     */
    public static final int ADD_PASS                  = 1;

    /**
     * getCURRENT_NUMBER_OF_PASSENGERS()
     */
    public static final int NUMBER_PASS               = 2;

    /**
     * lastPassenger()
     */
    public static final int LAST_PASS                 = 3;

    /**
     * waitingForLastPassenger()
     */
    public static final int WAITING_FOR_LAST_PASS     = 4;

    /**
     * Indicates that the passenger is the last one to attempt to exit the Airport
     */
    public static final int IS_LAST_PASS              = 5;

    /**
     * Indicates that the passenger is not the last one to attempt to exit the Airport
     */
    public static final int IS_NOT_LAST_PASS          = 6;

     /**
     * ACKNOWLEDGE
     */
    public static final int ACK                       = 7;

    /**
     * SHUTDOWN
     */
    public static final int SHUT                      = 8;

    /* Campos das mensagens */

    /**
     * Tipo da mensagem
     */

    private int msgType = -1;

    /**
     * identificador do passageiro
     */
    private int passengerID = -1;

    /**
     * número de passageiros no DepartureTerminal
     */
    private int passengersDeparture = -1;

     /**
     * número de passageiros no ArrivalTerminalExit
     */
    private int currentNumberOfPassengers = -1;

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
     * @param numberOfPass_t Número de passageiros atualmente no ArrivalTerminalExit
     */

    public Message(int type, int numberOfPass_t) 
    {
        msgType = type;
        currentNumberOfPassengers = numberOfPass_t;
    }

    /**
     * Instanciação de uma mensagem (forma 3).
     *
     * @param type  tipo da mensagem
     * @param id  thread number
     * @param curr number of passengers in DepartureTerminal
     */

    public Message(int type, int id, int curr)
     {
        msgType = type;
        this.passengerID = id;
        this.passengersDeparture = curr;
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
     * Obtenção do ID do passageiro
     *
     * @return ID do passageiro
     */

    public int getPassengerID() 
    {
        return passengerID;
    }

    /**
     * Obtenção do valor do número de passageiros no Departure Terminal
     *
     * @return número de passageiros no Departure Terminal
     */

    public int getPassengersDeparture() 
    {
        return passengersDeparture;
    }

    /**
     * Obtenção do valor do número de passageiros no ArrivalTerminalExit
     *
     * @return número de passageiros no ArrivalTerminalExit
     */

    public int getCurrentNumberOfPassengers() 
    {
        return currentNumberOfPassengers;
    }

    /**
     * Impressão dos campos internos. Usada para fins de debugging.
     *
     * @return string contendo, em linhas separadas, a concatenação da identificação
     *         de cada campo e valor respectivo
     */

    @Override
    public String toString() 
    {
        return ("Tipo = " + msgType
        + "\nID do passageiro = "  + passengerID
        + "\nNúmero de passageiros no Departure Terminal = " + passengersDeparture
        + "\nNúmero de passageiros atuais (ArrivalTerminalExit)" + currentNumberOfPassengers);
    }
}
