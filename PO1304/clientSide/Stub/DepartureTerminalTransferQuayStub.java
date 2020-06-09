package clientSide.Stub;

import common_infrastructures.SBusDriver;
import common_infrastructures.SPassenger;
import shared_regions_JavaInterfaces.IDepartureTerminalTransferQuayBusDriver;
import shared_regions_JavaInterfaces.IDepartureTerminalTransferQuayPassenger;

public class DepartureTerminalTransferQuayStub
        implements IDepartureTerminalTransferQuayBusDriver, IDepartureTerminalTransferQuayPassenger {

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

    public DepartureTerminalTransferQuayStub(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    @Override
    public SPassenger leaveTheBus(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SBusDriver parkTheBusAndLetPassOff(Integer n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public SBusDriver goToArrivalTerminal() {
		// TODO Auto-generated method stub
		return null;
    }
}