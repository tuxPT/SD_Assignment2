package clientSide.Stub;

import common_infrastructures.SPassenger;
import shared_regions_JavaInterfaces.IArrivalTerminalTransferQuayBusDriver;
import shared_regions_JavaInterfaces.IArrivalTerminalTransferQuayPassenger;

public class ArrivalTerminalTransferQuayStub
        implements IArrivalTerminalTransferQuayBusDriver, IArrivalTerminalTransferQuayPassenger {

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

    public ArrivalTerminalTransferQuayStub(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    @Override
    public SPassenger enterTheBus(Integer Passenger_ID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean announcingBusBoarding() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Integer goToDepartureTerminal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void endOfWork() {
        // TODO Auto-generated method stub

    }
}