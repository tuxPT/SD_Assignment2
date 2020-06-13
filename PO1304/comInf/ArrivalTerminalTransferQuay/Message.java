package comInf.ArrivalTerminalTransferQuay;

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

    /* Passenger */
    /**
     * enterTheBus(Integer Passenger_ID)
     */
    public static final int ENTER_BUS           = 1;

    /* Bus Driver */
    /**
     * announcingBusBoarding()
     */ 
    public static final int ANN_BUS_BOARD       = 2;

    /**
     * goToDepartureTerminal()
     */
    public static final int GO2DEP_TERMINAL     = 3;

    /**
     * endOfWork()
     */
    public static final int END_OF_WORK         = 4;

    /**
     * Indicates that there is no more work left to do
     */
    public static final int NO_MORE_WORK        = 5;

    /**
     * Indicates that the bus is leaving
     */
    public static final int BUS_LEAVING         = 6;

     /**
     * ACKNOWLEDGE
     */
    public static final int ACK                 = 7;

    /**
     * SHUTDOWN
     */
    public static final int SHUT                = 8;

    /**
     * SPassenger State TERMINAL_TRANSFER
     */
    public static final int STATE_TRT = 5;

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
     * Número de passageiro a bordo do autocarro
     */
    private int passengersOnBoard = -1;

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
     * @param n   identificador do passageiro ou número de passageiros a bordo do autocarro
     */

    public Message(int type, int n) 
    {
        msgType = type;
        if(type == ENTER_BUS){
            this.passengerID = n;
        }
        else if(type == ACK){
            this.passengersOnBoard = n;
        }
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
     * Obtenção do valor do ID do passageiro
     *
     * @return ID do passageiro
     */

    public int getPassengerID()
    {
        return passengerID;
    }

     /**
     * Obtenção do número de passageiros a bordo do autocarro
     *
     * @return Número de passageiros a bordo do autocarro
     */

    public int getNumberOfPassengersOnBoard()
    {
        return passengersOnBoard;
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
        + "\nID do passageiro = " + passengerID
        + "\nNúmero de passageiros a bordo do autocarro = " + passengersOnBoard);
    }
}
