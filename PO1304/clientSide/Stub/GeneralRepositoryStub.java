package clientSide.Stub;

import common_infrastructures.SBusDriver;
import common_infrastructures.SPassenger;
import common_infrastructures.SPorter;
import shared_regions_JavaInterfaces.IGeneralRepository;

public class GeneralRepositoryStub implements IGeneralRepository {

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

    public GeneralRepositoryStub(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    @Override
    public void updateBusDriver(SBusDriver Stat, boolean print) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePassenger(SPassenger Stat, Integer id, Boolean addWaitingQueue, Boolean addBusSeats,
            Integer startBags, Boolean collectBags, Boolean transit) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePorter(SPorter Stat, Integer BN, Integer CB, Integer SR, boolean print) {
        // TODO Auto-generated method stub

    }

    @Override
    public void nextFlight() {
        // TODO Auto-generated method stub

    }

    @Override
    public void endOfLifePlane() {
        // TODO Auto-generated method stub

    }

    @Override
	public void printRepository() {
		// TODO Auto-generated method stub
		
    }
}