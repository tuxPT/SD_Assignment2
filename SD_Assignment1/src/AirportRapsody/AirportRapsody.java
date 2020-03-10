package AirportRapsody;

public class AirportRapsody {
    public static void main(String[] args) throws Exception {
        Integer MAX_PASSENGERS, MAX_PORTER, MAX_BUSDRIVER, PLANES_PER_DAY;
        MAX_PASSENGERS = 6;
        MAX_PORTER = 1;
        MAX_BUSDRIVER = 1;
        PLANES_PER_DAY = 5;
        Boolean END_OF_DAY = false;

        for(int i = 0; i<PLANES_PER_DAY; i++) {
            for(int j = 0; j<MAX_PASSENGERS; j++) {
                createPassenger();
                createBusDriver();
                createPorter();
            }
        }
    }

    private void createPassenger() {
        //pass
    }

    private void createBusDriver() {
        //pass
    }
    private void createPorter() {
        //pass
    }
}