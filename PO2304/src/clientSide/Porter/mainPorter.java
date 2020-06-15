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
    private static String AL_HOST;
    private static int AL_PORT;
    private static String BCP_HOST;
    private static int BCP_PORT;
    private static String TSA_HOST;
    private static int TSA_PORT;

    public static void main(String[] args){
        AL_HOST = args[0];
        AL_PORT = Integer.parseInt(args[1]);
        BCP_HOST = args[2];
        BCP_PORT = Integer.parseInt(args[3]);
        TSA_HOST = args[4];
        TSA_PORT = Integer.parseInt(args[5]);
        TPorter[] TPorter = new TPorter[MAX_PORTER]; // array de threads cliente
        ArrivalLoungeStub arrivalLoungeStub; // stub à barbearia
        BaggageCollectionPointStub baggageCollectionPointStub;
        TemporaryStorageAreaStub temporaryStorageAreaStub;
        String serverHostName; // nome do sistema computacional onde está o servidor
        int serverPortNumb; // número do port de escuta do servidor

        arrivalLoungeStub = new ArrivalLoungeStub(AL_HOST, AL_PORT);
        baggageCollectionPointStub = new BaggageCollectionPointStub(BCP_HOST, BCP_PORT);
        temporaryStorageAreaStub = new TemporaryStorageAreaStub(TSA_HOST, TSA_PORT);
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