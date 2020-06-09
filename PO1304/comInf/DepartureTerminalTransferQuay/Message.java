package comInf.DepartureTerminalTransferQuay;

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

    /* Tipos das mensagens */
    /* Bus Driver */
    /**
    * SBusDriver parkTheBusAndLetPassOff(Integer n)
    */
    public static final int PBLPF                = 1;

    /**
    * SBusDriver goToArrivalTerminal()
    */
    public static final int GTAT                 = 2;

    /* Passenger */
    /**
    * SPassenger leaveTheBus(Integer id)
    */
    public static final int LTB                  = 3;

    /**
    * SBusDriver State DRIVING_BACKWARD
    */
    public static final int STATE_DB             = 4;

    /**
    * SBusDriver State PARKING_AT_THE_ARRIVAL_TERMINAL
    */
    public static final int STATE_PKAT           = 5;

    /**
    * SPassenger State AT_THE_DEPARTURE_TRANSFER_TERMINAL
    */
    public static final int STATE_DTT            = 6;

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
     *  Número de passageiros a bordo
     */

    private int numberOfPassengersOnBus = -1;

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
     * @param id identificador do passageiro
     */

    public Message (int type, Integer idn)
    {
        if (type == Message.PBLPF)
        {
            numberOfPassengersOnBus = idn;
        }
        else
        {
            passengerID = idn;
        }
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
     *  Obtenção do valor do campo com o número de passageiros no autocarro.
     *
     *    @return número de passageiros no autocarro
     */

    public int getNumberOfPassengersOnBus() 
    {
        return numberOfPassengersOnBus;
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
      return ("Tipo = " + msgType +
              "\nId Passageiro = " + passengerID +
              "\nNúmero de passageiros no autocarro = " + numberOfPassengersOnBus);
   }
}
