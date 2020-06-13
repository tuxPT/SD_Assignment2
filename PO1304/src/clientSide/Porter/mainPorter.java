package clientSide.Porter;

import clientSide.Stub.ArrivalLoungeStub;
import clientSide.Stub.BaggageCollectionPointStub;
import clientSide.Stub.TemporaryStorageAreaStub;
import entities.TPorter;
import shared_regions_JavaInterfaces.IArrivalLoungePorter;
import shared_regions_JavaInterfaces.IBaggageCollectionPointPorter;
import shared_regions_JavaInterfaces.ITemporaryStorageAreaPorter;

public class mainPorter {
    private static int MAX_PORTER = 1;
    public static void main(String[] args){
        TPorter[] TPorter = new TPorter[MAX_PORTER]; // array de threads cliente
        ArrivalLoungeStub arrivalLoungeStub; // stub à barbearia
        BaggageCollectionPointStub baggageCollectionPointStub;
        TemporaryStorageAreaStub temporaryStorageAreaStub;
        String serverHostName; // nome do sistema computacional onde está o servidor
        int serverPortNumb; // número do port de escuta do servidor

        arrivalLoungeStub = new ArrivalLoungeStub("localhost", 20010);
        baggageCollectionPointStub = new BaggageCollectionPointStub("localhost", 20040);
        temporaryStorageAreaStub = new TemporaryStorageAreaStub("localhost", 20090);
        /* Criação dos threads barbeiro e cliente */
        for (int i = 0; i < MAX_PORTER; i++) {
            TPorter[i] = new TPorter(i, (IArrivalLoungePorter) arrivalLoungeStub,
                    (IBaggageCollectionPointPorter) baggageCollectionPointStub,
                    (ITemporaryStorageAreaPorter) temporaryStorageAreaStub);
            // executa o run
            TPorter[i].start();
        }
        for (int i = 0; i < TPorter.length; i++) {
            try {
                TPorter[i].join();
            } catch (InterruptedException e) {
            }
        }
        arrivalLoungeStub.setPorterOut();
        temporaryStorageAreaStub.shutdown();
    }
}