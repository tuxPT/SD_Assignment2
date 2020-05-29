package comInf.ArrivalLounge;

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
   // whatShouldIDo(pthread_number, bags.size(), TRANSIT);
   public static final int WSD                  =  1;

   /* Porter */
   // takeARest()
   public static final int TAKE_REST            =  2;

   // tryToCollectABag()
   public static final int TRY_COLLECT          =  3;

   // noMoreBagsToCollect();
   public static final int NO_MORE_BAGS         =  4;

   // carryItToAppropriateStore(Bag bag);
   public static final int CARRY_TO_APP_STORE   =  5;

   // SPassenger State AT_THE_ARRIVAL_TRANSFER_TERMINAL
   public static final int STATE_ATT            = 6;

   // SPassenger State AT_THE_LUGGAGE_COLLECTION_POINT
   public static final int STATE_LCP            = 7;

   // SPassenger State EXITING_THE_ARRIVAL_TERMINAL
   public static final int STATE_EAT            = 8;


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
