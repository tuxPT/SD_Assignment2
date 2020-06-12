package clientSide.Stub;

import clientSide.ClientCom;
import common_infrastructures.SPassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalPassenger;
import comInf.DepartureTerminal.Message;

public class DepartureTerminalStub implements IDepartureTerminalPassenger {

    /**
     * Nome do sistema computacional onde está localizado o servidor
     * 
     * @serialField serverHostName
     */

    private String serverHostName = null;

    /**
     * Número do port de escuta do servidor
     * 
     * @serialField serverPortNumb
     */

    private int serverPortNumb;

    /**
     * Instanciação do stub à barbearia.
     *
     * @param hostName nome do sistema computacional onde está localizado o servidor
     * @param port     número do port de escuta do servidor
     */

    public DepartureTerminalStub(String hostName, int port) {
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
        outMessage = new Message(Message.GET_NP); // pede a realização do serviço
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
	public SPassenger prepareNextLeg() {
		ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.PNL); // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.STATE_EDT){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return SPassenger.ENTERING_THE_DEPARTURE_TERMINAL;
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
        outMessage = new Message(Message.LP); // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}