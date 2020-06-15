package clientSide.Stub;

import common_infrastructures.SPassenger;
import shared_regions_JavaInterfaces.IBaggageReclaimOfficePassenger;
import clientSide.ClientCom;
import comInf.BaggageReclaimOffice.Message;

public class BaggageReclaimOfficeStub implements IBaggageReclaimOfficePassenger {

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

    public BaggageReclaimOfficeStub(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    @Override
    public SPassenger addBag(Integer id, int i) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
    
        while (!con.open()) // aguarda ligação
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.ADD_BAG, id, i); // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.STATE_EAT)){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
             
        return SPassenger.EXITING_THE_ARRIVAL_TERMINAL;
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