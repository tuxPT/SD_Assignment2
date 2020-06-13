package clientSide.Stub;

import shared_regions_JavaInterfaces.IArrivalTerminalExitPassenger;
import clientSide.ClientCom;
import comInf.ArrivalTerminalExit.Message;

/**
 * Este tipo de dados define o stub à barbearia numa solução do Problema dos
 * Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class ArrivalTerminalExitStub implements IArrivalTerminalExitPassenger
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

   public ArrivalTerminalExitStub(String hostName, int port)
   {
      serverHostName = hostName;
      serverPortNumb = port;
   }

  @Override
  public boolean addPassenger(Integer id, Integer curr) {
    ClientCom con = new ClientCom(serverHostName, serverPortNumb);
    Message inMessage, outMessage;

    while (!con.open()) // aguarda ligação
    {
        try {
            Thread.currentThread().sleep((long) (10));
        } catch (InterruptedException e) {
        }
    }
    outMessage = new Message(Message.ADD_PASS, id, curr); // pede a realização do serviço
    con.writeObject(outMessage);
    inMessage = (Message) con.readObject();
    if ((inMessage.getType() != Message.IS_LAST_PASS) && (inMessage.getType() != Message.IS_NOT_LAST_PASS)){
        System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
        System.out.println(inMessage.toString());
        System.exit(1);
    }
    con.close();
    
    if(inMessage.getType() == Message.IS_LAST_PASS){
        return true;
    }
    else{
        return false;
    }
  }

  @Override
  public void waitingForLastPassenger(){
    ClientCom con = new ClientCom(serverHostName, serverPortNumb);
    Message inMessage, outMessage;

    while (!con.open()) // aguarda ligação
    {
        try {
            Thread.currentThread().sleep((long) (10));
        } catch (InterruptedException e) {
        }
    }
    outMessage = new Message(Message.WAITING_FOR_LAST_PASS); // pede a realização do serviço
    con.writeObject(outMessage);
    inMessage = (Message) con.readObject();
    if ((inMessage.getType() != Message.ACK)){
        System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
        System.out.println(inMessage.toString());
        System.exit(1);
    }
    con.close();
  }

  @Override
  public Integer getCURRENT_NUMBER_OF_PASSENGERS() {
    ClientCom con = new ClientCom(serverHostName, serverPortNumb);
    Message inMessage, outMessage;

    while (!con.open()) // aguarda ligação
    {
        try {
            Thread.currentThread().sleep((long) (10));
        } catch (InterruptedException e) {
        }
    }
    outMessage = new Message(Message.WAITING_FOR_LAST_PASS); // pede a realização do serviço
    con.writeObject(outMessage);
    inMessage = (Message) con.readObject();
    if (inMessage.getType() != Message.ACK){
        System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
        System.out.println(inMessage.toString());
        System.exit(1);
    }
    con.close();

    if (inMessage.getCurrentNumberOfPassengers() < 0){
        System.out.println("Thread " + Thread.currentThread().getName() + ": Número de passageiros recebidos é inválido!");
        System.out.println(inMessage.toString());
        System.exit(1);
       
    }
    return inMessage.getCurrentNumberOfPassengers();
  }

  @Override
  public void lastPassenger() {
    ClientCom con = new ClientCom(serverHostName, serverPortNumb);
    Message inMessage, outMessage;

    while (!con.open()) // aguarda ligação
    {
        try {
            Thread.currentThread().sleep((long) (10));
        } catch (InterruptedException e) {
        }
    }
    outMessage = new Message(Message.LAST_PASS); // pede a realização do serviço
    con.writeObject(outMessage);
    inMessage = (Message) con.readObject();
    if (inMessage.getType() != Message.ACK){
        System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
        System.out.println(inMessage.toString());
        System.exit(1);
    }
    con.close();
  }

  // termina o processo
  public void shutdown() {
    ClientCom con = new ClientCom(serverHostName, serverPortNumb);
    Message inMessage, outMessage;

    while (!con.open()) // aguarda ligação
    {
        try {
            Thread.currentThread().sleep((long) (10));
        } catch (InterruptedException e) {
        }
    }
    outMessage = new Message(Message.SHUT);
    con.writeObject(outMessage);
    inMessage = (Message) con.readObject();
    if (inMessage.getType() != Message.ACK) {
        System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
        System.out.println(inMessage.toString());
        System.exit(1);
    }
    con.close();
}
}
