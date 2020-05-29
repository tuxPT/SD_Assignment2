package comInf.DepartureTerminal;

import java.io.*;
import common_infrastructures.Bag;

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
   /* Passenger */
   // boolean addPassenger(Integer id, Integer curr);
   public static final int ADD_PASS             = 1;

   // void waitingForLastPassenger(); 
   public static final int WFLP                 = 2;

   // Integer getCURRENT_NUMBER_OF_PASSENGERS();
   public static final int GET_NP               = 3;

   // void lastPassenger();
   public static final int LP                   = 4;

   // SPassenger prepareNextLeg();
   public static final int PNL                  = 5;

   //SPassenger State ENTERING_THE_DEPARTURE_TERMINAL
   public static final int STATE_EDT            = 6;

  /* Campos das mensagens */

  /**
   *  Tipo da mensagem
   */

   private int msgType = -1;

  /**
   *  Identificação do cliente
   */

   private int passengerID = -1;

   /**
   *  Identificação do cliente
   */

  private int bags = -1;

  /**
   *  Identificação do cliente
   */

  private boolean transit = false;

  /**
   *  Identificação do cliente
   */

  private Bag bag = null;


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
   *  Instanciação de uma mensagem (forma 3).
   *
   *    @param type tipo da mensagem
   *    @param bag mala
   *    @param custId identificação do cliente
   */

   public Message (int type, Bag bag)
   {
      msgType = type;
      this.bag = bag;
   }

  /**
   *  Instanciação de uma mensagem (forma 4).
   *
   *    @param type tipo da mensagem
   *    @param name nome do ficheiro de logging
   *    @param nIter número de iterações do ciclo de vida dos clientes
   */

   public Message (int type, Integer id, Integer t_bags, boolean t_TRANSIT)
   {
      msgType = type;
      this.passengerID = id;
      this.bags = t_bags;
      this.transit = t_TRANSIT;
   }

  /**
   *  Obtenção do valor do campo tipo da mensagem.
   *
   *    @return tipo da mensagem
   */

   public int getType ()
   {
      return (msgType);
   }

  /**
   *  Obtenção do valor do campo identificador do cliente.
   *
   *    @return identificação do cliente
   */

   public int getPassengerID ()
   {
      return (this.getPassengerID());
   }

/**
   *  Obtenção do valor do campo identificador do cliente.
   *
   *    @return identificação do cliente
   */

  public int getBags ()
  {
     return (this.bags);
  }

  /**
   *  Obtenção do valor do campo identificador do cliente.
   *
   *    @return identificação do cliente
   */

  public boolean getTransit ()
  {
     return (this.transit);
  }

  /**
   *  Obtenção do valor do campo identificador do cliente.
   *
   *    @return identificação do cliente
   */

  public Bag getBag ()
  {
     return (this.bag);
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
              "\nId Cliente = " + custId +
              "\nId Barbeiro = " + barbId +
              "\nNome Fic. Logging = " + fName +
              "\nN. de Iteracoes = " + nIter);
   }
}
