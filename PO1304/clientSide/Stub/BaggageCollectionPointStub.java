package clientSide.Stub;

import java.util.List;

import common_infrastructures.Bag;
import common_infrastructures.SPorter;
import shared_regions_JavaInterfaces.IBaggageCollectionPointPassenger;
import shared_regions_JavaInterfaces.IBaggageCollectionPointPorter;

public class BaggageCollectionPointStub implements IBaggageCollectionPointPassenger, IBaggageCollectionPointPorter {

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

    public BaggageCollectionPointStub(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    @Override
    public void warnPassengers() {
        // TODO Auto-generated method stub

    }

    @Override
    public SPorter addBag(Bag bag) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer goCollectABag(Integer id, List<Integer> t) {
        // TODO Auto-generated method stub
        return null;
    }
}