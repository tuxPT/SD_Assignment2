package serverSide.shared_regions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import common_infrastructures.SBusDriver;
import common_infrastructures.SPassenger;
import common_infrastructures.SPorter;
import shared_regions_JavaInterfaces.IGeneralRepository;

public class MGeneralRepository implements IGeneralRepository {
    Integer FN, BN, CB, SR;
    SBusDriver busDriverStat;
    SPorter porterStat;
    SPassenger[] passengerStat;
    Boolean[] transit;
    Integer[] startTotalBags;
    Integer[] passengerCollectedBags;
    Queue<Integer> waitingQueue;
    Integer[] busSeats;
    Integer busSeatSize;
    //Final Report
    Integer nNonTransit, ntransit, totalBags, lostBags;

    ReentrantLock lock = new ReentrantLock(true);

    
    public MGeneralRepository(Integer PLANE_PASSENGERS, Integer BUS_CAPACITY) {
        this.FN = 0;
        this.BN = 0;
        this.CB = 0;
        this.SR = 0;

        this.passengerStat = new SPassenger[PLANE_PASSENGERS];
        this.transit = new Boolean[PLANE_PASSENGERS];
        this.startTotalBags = new Integer[PLANE_PASSENGERS];
        this.passengerCollectedBags = new Integer[PLANE_PASSENGERS];
        Arrays.fill(this.passengerCollectedBags, null);
        this.waitingQueue = new LinkedList<Integer>();
        this.busSeats = new Integer[BUS_CAPACITY];
        this.busSeatSize = 0;
        this.nNonTransit = 0;
        this.ntransit = 0;
        this.totalBags = 0;
        this.lostBags = 0;

        String stat = String.format("               AIRPORT RHAPSODY - Description of the internal state of the problem\n" +
                "\n" +
                "PLANE    PORTER                  DRIVER\n" +
                "FN BN  Stat CB SR   Stat  Q1 Q2 Q3 Q4 Q5 Q6  S1 S2 S3\n" +
                "                                                         PASSENGERS\n" +
                "St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6" +
                "\n");
        System.out.print(stat);
        write(stat);
    }

    public void printRepository(){
        String stat = String.format("Final report\n" +
                "N. of passengers which have this airport as their final destination = %2d\n" +
                "N. of passengers which are in transit = %2d\n" +
                "N. of bags that should have been transported in the the planes hold = %2d\n" +
                "N. of bags that were lost = %2d\n", nNonTransit, ntransit, totalBags, lostBags);
        System.out.printf(stat);
        write(stat);
    }

    private void write(String lines){
        try {
            Files.write(
                    Paths.get("log.txt"),
                    lines.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }


    @Override
    public void updatePorter(SPorter Stat, Integer BN, Integer CB, Integer SR, boolean print){
        lock.lock();
        try{
            this.BN = BN !=null ? BN : this.BN;
            this.CB = CB != null ? CB : this.CB;
            this.SR = SR != null ? SR : this.SR;
            this.porterStat = Stat != null ? Stat : this.porterStat;
            assert this.porterStat != null : "Stat porter is null";

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //print
            if(print){
                print();
            }
            lock.unlock();
        }
    }

    @Override
    public void updateBusDriver(SBusDriver Stat, boolean print){
        lock.lock();
        try{
            this.busDriverStat = Stat != null ? Stat : this.busDriverStat;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(print){
                print();
            }
            lock.unlock();
        }
    }

    @Override
    public void updatePassenger(SPassenger Stat, Integer id, Boolean addWaitingQueue, Boolean addBusSeats, Integer startBags, Boolean collectedBags, Boolean transit){
        lock.lock();
        try{
            this.passengerStat[id] = Stat != null ? Stat : this.passengerStat[id];
            if(addWaitingQueue != null){
                if(addWaitingQueue){
                    waitingQueue.add(id);
                }
                else{
                    waitingQueue.remove();
                }
            }
            if(addBusSeats != null){
                if(addBusSeats){
                    busSeats[busSeatSize] = id;
                    busSeatSize++;
                }
                else{
                    for(int i=0; i<busSeats.length; i++){
                        if(busSeats[i] != null && busSeats[i] == id){
                            busSeats[i] = null;
                            busSeatSize--;
                        }
                    }
                }
            }

            this.startTotalBags[id] = (startBags != null) ? startBags : this.startTotalBags[id];
            if(this.startTotalBags[id] != null) {
                if(this.passengerCollectedBags[id] == null){
                    this.passengerCollectedBags[id] = 0;
                }
                if(collectedBags == true){
                    CB--;
                    this.passengerCollectedBags[id]++;
                }
            }

            this.transit[id] = (transit != null) ? transit : this.transit[id];
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            print();
            lock.unlock();
        }
    }

    public void endOfLifePlane() {
        lock.lock();
        try{
            for(int i= 0; i<transit.length; i++){
                if(transit[i] != null){
                    if(transit[i]){
                        ntransit++;
                    }
                    else{
                        nNonTransit++;
                    }
                }
            }
            for(int i=0; i<startTotalBags.length; i++){
                if(startTotalBags[i] != null) {
                    totalBags += startTotalBags[i];
                    if(lostBags != null && transit[i] == false) {
                        lostBags += (startTotalBags[i] - passengerCollectedBags[i]);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public void nextFlight(){
        lock.lock();
        try{
            FN++;
            Arrays.fill(passengerStat, null);
            Arrays.fill(transit, null);
            Arrays.fill(startTotalBags, null);
            Arrays.fill(passengerCollectedBags, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    void print() {
        List<Integer> tempWaitingQueue = new LinkedList<>(waitingQueue);
        String stat = String.format("" +
                " %1d  %1d  %3s  %1d  %1d   %3s   %1s  %1s  %1s  %1s  %1s  %1s   %1s  %1s  %1s" +
                "\n" +
                "%3s %3s  %1s   %1s  %3.3s %3.3s  %1s   %1s  %3.3s %3.3s  %1s   %1s  %3.3s %3.3s  %1s   %1s  %3.3s %3.3s  %1s   %1s  %3.3s %3.3s  %1s   %1s" +
                "\n",
                FN, BN,
                mapPorterStat(porterStat), CB, SR,
                mapBusDriverStat(busDriverStat), oneHifen(tempWaitingQueue.size() > 0 ? tempWaitingQueue.get(0): null),
                oneHifen(tempWaitingQueue.size() > 1 ? tempWaitingQueue.get(1): null),
                oneHifen(tempWaitingQueue.size() > 2 ? tempWaitingQueue.get(2): null),
                oneHifen(tempWaitingQueue.size() > 3 ? tempWaitingQueue.get(3): null),
                oneHifen(tempWaitingQueue.size() > 4 ? tempWaitingQueue.get(4): null),
                oneHifen(tempWaitingQueue.size() > 5 ? tempWaitingQueue.get(5): null),
                oneHifen(busSeats[0]), oneHifen(busSeats[1]), oneHifen(busSeats[2]),
                mapPassenger(passengerStat[0]), mapTransit(transit[0]), IntegerOneHifen(startTotalBags[0]), IntegerOneHifen(passengerCollectedBags[0]),
                mapPassenger(passengerStat[1]), mapTransit(transit[1]), IntegerOneHifen(startTotalBags[1]), IntegerOneHifen(passengerCollectedBags[1]),
                mapPassenger(passengerStat[2]), mapTransit(transit[2]), IntegerOneHifen(startTotalBags[2]), IntegerOneHifen(passengerCollectedBags[2]),
                mapPassenger(passengerStat[3]), mapTransit(transit[3]), IntegerOneHifen(startTotalBags[3]), IntegerOneHifen(passengerCollectedBags[3]),
                mapPassenger(passengerStat[4]), mapTransit(transit[4]), IntegerOneHifen(startTotalBags[4]), IntegerOneHifen(passengerCollectedBags[4]),
                mapPassenger(passengerStat[5]), mapTransit(transit[5]), IntegerOneHifen(startTotalBags[5]), IntegerOneHifen(passengerCollectedBags[5])
                );
        System.out.print(stat);
        write(stat);
    }

    String mapTransit(Boolean t){
        if(t != null) {
            if (t) {
                return "TRT";
            } else {
                return "FDT";
            }
        }
        else{
            return "---";
        }
    }

    String oneHifen(Object o){
        if(o == null){
            return "-";
        }
        else{
            return o.toString();
        }
    }

    String IntegerOneHifen(Integer i){
        if(i == null){
            return "-";
        }
        else{
            return Integer.toString(i);
        }
    }


    String mapPorterStat(SPorter stat){
        if(stat != null) {
            switch (stat) {
                case WAITING_FOR_A_PLANE_TO_LAND:
                    return "WPTL";
                case AT_THE_PLANES_HOLD:
                    return "APLH";
                case AT_THE_LUGGAGE_BELT_CONVEYOR:
                    return "ALCB";
                case AT_THE_STOREROOM:
                    return "ASTR";
                default:
                    return "---";
            }
        }
        else{
            return "WPTL";
        }
    }

    String mapBusDriverStat(SBusDriver stat){
        if(stat != null) {
            switch (stat) {
                case PARKING_AT_THE_ARRIVAL_TERMINAL:
                    return "PKAT";
                case DRIVING_FORWARD:
                    return "DRFW";
                case PARKING_AT_THE_DEPARTURE_TERMINAL:
                    return "PKDT";
                case DRIVING_BACKWARD:
                    return "DRBW";
                default:
                    return "---";
            }
        }
        else{
            return "PKAT";
        }
    }

    String mapPassenger(SPassenger stat) {
        if (stat != null) {
            switch (stat) {
                case AT_THE_DISEMBARKING_ZONE:
                    return "WSD";
                case AT_THE_LUGGAGE_COLLECTION_POINT:
                    return "LCP";
                case AT_THE_BAGGAGE_RECLAIM_OFFICE:
                    return "BRO";
                case EXITING_THE_ARRIVAL_TERMINAL:
                    return "EAT";
                case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
                    return "ATT";
                case TERMINAL_TRANSFER:
                    return "TRT";
                case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
                    return "DTT";
                case ENTERING_THE_DEPARTURE_TERMINAL:
                    return "EDT";
                default:
                    return "---";
            }
        }
        else{
            return "---";
        }
    }

    public void setBags(int count) {
        this.BN = count;
    }
}
