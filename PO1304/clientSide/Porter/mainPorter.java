package clientSide.Porter;

public class mainPorter {
    public void main(String[] args){
        for (int i = 0; i < MAX_PORTER; i++) {
            TPorter[i] = new TPorter(i, (IArrivalLoungePorter) MArrivalLounge,
                    (IBaggageCollectionPointPorter) MBaggageCollectionPoint,
                    (ITemporaryStorageAreaPorter) MTemporaryStorageArea);
            // executa o run
            TPorter[i].start();
        }
        for (int i = 0; i < TPorter.length; i++) {
            try {
                MArrivalLounge.endOfWork();
                TPorter[i].join();

            } catch (InterruptedException e) {
            }
        }
    }
}