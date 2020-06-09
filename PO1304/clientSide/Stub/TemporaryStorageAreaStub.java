package clientSide.Stub;

import common_infrastructures.SPorter;
import shared_regions_JavaInterfaces.ITemporaryStorageAreaPorter;

public class TemporaryStorageAreaStub implements ITemporaryStorageAreaPorter {

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

    public TemporaryStorageAreaStub(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    @Override
	public SPorter addBag() {
		// TODO Auto-generated method stub
		return null;
    }
}