package clientSide.BusDriver;

public class mainBusDriver {
    public void main(String[] args){
        for (int i = 0; i < MAX_BUSDRIVER; i++) {
            TBusDriver[i] = new TBusDriver(i, (IArrivalTerminalTransferQuayBusDriver) MArrivalTerminalTransferQuay,
                    (IDepartureTerminalTransferQuayBusDriver) MDepartureTerminalTransferQuay);
            // executa o run
            TBusDriver[i].start();
        }
        for (int i = 0; i < TBusDriver.length; i++) {
            try {
                MArrivalTerminalTransferQuay.endOfWork();
                TBusDriver[i].join();
            } catch (InterruptedException e) {
            }
        }
    }
}