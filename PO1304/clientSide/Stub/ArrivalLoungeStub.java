package clientSide.Stub;

import clientSide.ClientCom;
import comInf.ArrivalLounge.Message;
import common_infrastructures.Bag;
import common_infrastructures.SPassenger;
import common_infrastructures.SPorter;
import shared_regions_JavaInterfaces.IArrivalLoungePassenger;
import shared_regions_JavaInterfaces.IArrivalLoungePorter;

/**
 * Este tipo de dados define o stub à barbearia numa solução do Problema dos
 * Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class ArrivalLoungeStub implements IArrivalLoungePassenger, IArrivalLoungePorter {

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

    public ArrivalLoungeStub(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    @Override
    public Bag tryToCollectABag() {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.TRY_COLLECT); // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.COLLECT_DONE) && (inMessage.getType() != Message.NO_BAGS)) {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.getType() == Message.COLLECT_DONE){
            return inMessage.getBag();
        }
        else{
            return null;
        }
    }

    @Override
    public boolean takeARest() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public SPorter noMoreBagsToCollect() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SPorter carryItToAppropriateStore(Bag bag) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SPassenger whatShouldIDo(Integer id, Integer t_bags, boolean t_TRANSIT) {
        // TODO Auto-generated method stub
        return null;
    }

    // função no main
    public void waitForPorter(){
        
    }

    // função no main
    public void endOfWork(){
        
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
