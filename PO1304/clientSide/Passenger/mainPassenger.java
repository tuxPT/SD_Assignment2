package clientSide.Passenger;

public class mainPassenger {
    public void main(String[] args){
        for(int p=0; p<PLANES_PER_DAY; p++) {
            MGeneralRepository.nextFlight();
            //total bags generator
            bags = generateBags(PLANE_PASSENGERS, MAX_BAGS_NUMBER);
            //lost bags generator
            int count = 0;
            int total_bags = 0;
            for(int i=0; i<bags.length; i++){
                total_bags += bags[i].size();
                for(int j=0; j<bags[i].size(); j++){
                    Integer probability = random.nextInt(100);
                    //5% of lost bags
                    if(probability > 5){
                        MArrivalLounge.addBag(bags[i].get(j));
                        count++;
                    }
                }
            }

            MGeneralRepository.setBags(count);
            MBaggageCollectionPoint.newPlane();

            for (int i = 0; i < PLANE_PASSENGERS; i++) {
                Random r = new Random();
                boolean t_TRANSIT;
                if (bags[i].size() != 0){
                    t_TRANSIT = bags[i].get(0).getTRANSIT();
                }
                else{
                    t_TRANSIT = r.nextBoolean();
                }
                List<Integer> temp = new LinkedList<Integer>();
                for(Bag b: bags[i]){
                    temp.add(b.getID());
                }
                //instancia
                TPassenger[i] = new TPassenger(i,t_TRANSIT, temp, PLANE_PASSENGERS,
                        (IArrivalLoungePassenger) MArrivalLounge, (IArrivalTerminalExitPassenger) MArrivalTerminalExit,
                        (IArrivalTerminalTransferQuayPassenger) MArrivalTerminalTransferQuay,
                        (IBaggageCollectionPointPassenger) MBaggageCollectionPoint,
                        (IBaggageReclaimOfficePassenger) MBaggageReclaimOffice,
                        (IDepartureTerminalTransferQuayPassenger) MDepartureTerminalTransferQuay,
                        (IDepartureTerminalPassenger) MDepartureTerminal);
                // executa o run
                TPassenger[i].start();
            }

            // Wait for joins
            for (int i = 0; i < TPassenger.length; i++) {
                try {
                    TPassenger[i].join();
                } catch (InterruptedException e) {
                }
            }
            MArrivalLounge.waitForPorter();
            MGeneralRepository.endOfLifePlane();
        }
        MGeneralRepository.printRepository();
    }

    private static ArrayList<Bag>[] generateBags(int PLANE_PASSENGERS, int MAX_BAGS_NUMBER) {
        // gerar numero de malas para o passageiro
        Random random = new Random();
        Integer size;

        ArrayList<Bag>[] bags = new ArrayList[PLANE_PASSENGERS];
        for (int i = 0; i < PLANE_PASSENGERS; i++)
        {
            bags[i] = new ArrayList<Bag>();
        }

        Boolean TRANSIT;
        // atribuir um id unico da mala ao passageiro
        for(int i=0; i<PLANE_PASSENGERS; i++) {
            TRANSIT = random.nextBoolean();
            size = random.nextInt(MAX_BAGS_NUMBER + 1);
            for (int nbag = 0; nbag < size; nbag++) {
                bags[i].add(new Bag(i * MAX_BAGS_NUMBER + nbag, TRANSIT));
            }
        }
        return bags;
    }
}