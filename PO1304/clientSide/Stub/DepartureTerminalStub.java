package clientSide.Stub;

import common_infrastructures.SPassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalPassenger;

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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Integer getCURRENT_NUMBER_OF_PASSENGERS() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void lastPassenger() {
        // TODO Auto-generated method stub

    }

    @Override
	public SPassenger prepareNextLeg() {
		// TODO Auto-generated method stub
		return null;
    }
}