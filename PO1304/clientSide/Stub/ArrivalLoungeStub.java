package clientSide;

import genclass.GenericIO;
import shared_regions.IArrivalLoungePassenger;
import shared_regions.IArrivalLoungePorter;
import comInf.Message;

/**
 *   Este tipo de dados define o stub à barbearia numa solução do Problema dos Barbeiros Sonolentos que implementa
 *   o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class BarberShopStub implements IArrivalLoungePassenger, IArrivalLoungePorter
{

  /**
   *  Nome do sistema computacional onde está localizado o servidor
   *    @serialField serverHostName
   */

   private String serverHostName = null;

  /**
   *  Número do port de escuta do servidor
   *    @serialField serverPortNumb
   */

   private int serverPortNumb;

  /**
   *  Instanciação do stub à barbearia.
   *
   *    @param hostName nome do sistema computacional onde está localizado o servidor
   *    @param port número do port de escuta do servidor
   */

   public BarberShopStub (String hostName, int port)
   {
      serverHostName = hostName;
      serverPortNumb = port;
   }

  /**
   *  Ir cortar cabelo (solicitação do serviço).
   *
   *    @param customerId identificação do cliente
   *
   *    @return <li>true, se conseguiu cortar o cabelo
   *            <li>false, em caso contrário
   */

   public boolean goCutHair (int customerId)
   {
      ClientCom con = new ClientCom (serverHostName, serverPortNumb);
      Message inMessage, outMessage;

      while (!con.open ())                                  // aguarda ligação
      { try
        { Thread.currentThread ().sleep ((long) (10));
        }
        catch (InterruptedException e) {}
      }
      outMessage = new Message (Message.REQCUTH, customerId);        // pede a realização do serviço
      con.writeObject (outMessage);
      inMessage = (Message) con.readObject ();
      if ((inMessage.getType () != Message.CUTHDONE) && (inMessage.getType () != Message.BSHOPF))
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      con.close ();

      if (inMessage.getType () == Message.CUTHDONE)
         return true;                                                // operação bem sucedida - corte efectuado
         else return false;                                          // operação falhou - barbearia cheia
  }

  /**
   *  Alertar o barbeiro do fim de operações (solicitação do serviço).
   *
   *    @param barberId identificação do barbeiro
   */

   public void sendInterrupt (int barberId)
   {
     ClientCom con = new ClientCom (serverHostName, serverPortNumb);
     Message inMessage, outMessage;

     while (!con.open ())                                  // aguarda ligação
     { try
       { Thread.currentThread ().sleep ((long) (10));
       }
       catch (InterruptedException e) {}
     }
     outMessage = new Message (Message.ENDOP, barberId);   // alertar barbeiro do fim de operações
     con.writeObject (outMessage);
     inMessage = (Message) con.readObject ();
     if (inMessage.getType () != Message.ACK)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
          GenericIO.writelnString (inMessage.toString ());
          System.exit (1);
        }
     con.close ();
   }

  /**
   *  Ir dormir (solicitação do serviço).
   *
   *    @param barberId identificação do barbeiro
   *
   *    @return <li>true, se ocorreu o fim de operações
   *            <li>false, em caso contrário
   */

   public boolean goToSleep (int barberId)
   {
      ClientCom con = new ClientCom (serverHostName, serverPortNumb);
      Message inMessage, outMessage;

      while (!con.open ())                                     // aguarda ligação
      { try
        { Thread.currentThread ().sleep ((long) (10));
        }
        catch (InterruptedException e) {}
      }
      outMessage = new Message (Message.GOTOSLP, barberId);     // o barbeiro vai dormir
      con.writeObject (outMessage);
      inMessage = (Message) con.readObject ();
      if ((inMessage.getType () != Message.CONT) && (inMessage.getType () != Message.END))
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName ()+ ": Tipo inválido!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      con.close ();

      if (inMessage.getType () == Message.END)
         return true;                                          // fim de operações
         else return false;                                    // continuação de operações
   }

  /**
   *  Chamar cliente (solicitação do serviço).
   *
   *    @param barberId identificação do barbeiro
   *
   *    @return identificação do cliente
   */

   public int callCustomer (int barberId)
   {
      ClientCom con = new ClientCom (serverHostName, serverPortNumb);
      Message inMessage, outMessage;

      while (!con.open ())                                      // aguarda ligação
      { try
        { Thread.currentThread ().sleep ((long) (10));
        }
        catch (InterruptedException e) {}
      }
      outMessage = new Message (Message.CALLCUST, barberId);    // o barbeiro chama o cliente
      con.writeObject (outMessage);
      inMessage = (Message) con.readObject ();
      if (inMessage.getType () != Message.CUSTID)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      con.close ();

      return inMessage.getCustId ();
   }

  /**
   *  Receber pagamento (solicitação do serviço).
   *
   *    @param barberId identificação do barbeiro
   *    @param customerId identificação do cliente
   */

   public void getPayment (int barberId, int customerId)
   {
      ClientCom con = new ClientCom (serverHostName, serverPortNumb);
      Message inMessage, outMessage;

      while (!con.open ())                                                // aguarda ligação
      { try
        { Thread.currentThread ().sleep ((long) (10));
        }
        catch (InterruptedException e) {}
      }
      outMessage = new Message (Message.GETPAY, barberId, customerId);    // o barbeiro recebe o pagamento
      con.writeObject (outMessage);
      inMessage = (Message) con.readObject ();
      if (inMessage.getType () != Message.ACK)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      con.close ();
   }

  /**
   *  Fornecer parâmetros do problema (solicitação do serviço).
   *
   *    @param barberId identificação do barbeiro
   *    @param customerId identificação do cliente
   */

   public void probPar (String fName, int nIter)
   {
      ClientCom con = new ClientCom (serverHostName, serverPortNumb);
      Message inMessage, outMessage;

      while (!con.open ())                                                // aguarda ligação
      { try
        { Thread.currentThread ().sleep ((long) (10));
        }
        catch (InterruptedException e) {}
      }
      outMessage = new Message (Message.SETNFIC, fName, nIter);
      con.writeObject (outMessage);
      inMessage = (Message) con.readObject ();
      if (inMessage.getType() != Message.NFICDONE)
         { GenericIO.writelnString ("Arranque da simulação: Tipo inválido!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      con.close ();
   }

  /**
   *  Fazer o shutdown do servidor (solicitação do serviço).
   */

   public void shutdown ()
   {
      ClientCom con = new ClientCom (serverHostName, serverPortNumb);
      Message inMessage, outMessage;

      while (!con.open ())                                                // aguarda ligação
      { try
        { Thread.currentThread ().sleep ((long) (10));
        }
        catch (InterruptedException e) {}
      }
      outMessage = new Message (Message.SHUT);
      con.writeObject (outMessage);
      inMessage = (Message) con.readObject ();
      if (inMessage.getType () != Message.ACK)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      con.close ();
   }
}
