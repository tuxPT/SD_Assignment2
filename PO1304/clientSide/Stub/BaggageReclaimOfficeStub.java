package clientSide.Stub;

import common_infrastructures.SPassenger;
import shared_regions_JavaInterfaces.IBaggageReclaimOfficePassenger;

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public SPassenger goHome(Integer id) {
		// TODO Auto-generated method stub
		return null;
    }
}