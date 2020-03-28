package AirportRapsody.Monitor;

import AirportRapsody.State.SBusDriver;
import AirportRapsody.State.SPassenger;
import AirportRapsody.State.SPorter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class MGeneralRepository {
    List<String> log;
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

    ReentrantLock lock = new ReentrantLock();

    
    public MGeneralRepository() {
        FN = 0;
        log = new LinkedList<String>();
        log.add("               AIRPORT RHAPSODY - Description of the internal state of the problem\n" +
                "\n" +
                "PLANE    PORTER                  DRIVER\n" +
                "FN BN  Stat CB SR   Stat  Q1 Q2 Q3 Q4 Q5 Q6  S1 S2 S3\n" +
                "                                                         PASSENGERS\n" +
                "St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6" +
                "\n");
        System.out.print(log.get(0));
    }

    private void printRepository(){

    }

    void updatePorter(SPorter Stat, Integer BN, Integer CB, Integer SR){
        lock.lock();
        try{
            this.BN = BN != -1 ? BN : this.BN;
            this.CB = CB != -1 ? CB : this.CB;
            this.SR = SR != -1 ? SR : this.SR;
            this.porterStat = Stat != null ? Stat : this.porterStat;

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //print
            lock.unlock();
        }
    }

    void updateBusDriver(SBusDriver Stat){
        lock.lock();
        try{
            this.busDriverStat = Stat != null ? Stat : this.busDriverStat;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //print
            print();
            lock.unlock();
        }
    }

    void updatePassenger(SPassenger Stat, Integer id, Boolean addWaitingQueue, Boolean addBusSeats, Integer startBags, Integer collectedBags, Boolean transit){
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
                        if(busSeats[i] == id){
                            busSeats[i] = null;
                            busSeatSize--;
                        }
                    }
                }
            }
            this.startTotalBags[id] = (startBags != null) ? startBags: this.startTotalBags[id];
            this.passengerCollectedBags[id] = (collectedBags != null) ? collectedBags : this.passengerCollectedBags[id];
            this.transit[id] = (transit != null) ? transit : this.transit[id];
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            print();
            lock.unlock();
        }
    }

    void reset(){
        lock.lock();
        try{
            for(int i= 0; i<transit.length; i++){
                if(transit[i]){
                    ntransit++;
                }
                else{
                    nNonTransit++;
                }
            }
            for(int i=0; i<startTotalBags.length; i++){
                totalBags+=startTotalBags[i];
                lostBags += (startTotalBags[i] - passengerCollectedBags[i]);
            }
            FN++;
            BN = null;
            CB = null;
            SR = null;
            busDriverStat = null;
            porterStat= null;
            Arrays.fill(passengerStat, null);
            Arrays.fill(transit, null);
            Arrays.fill(startTotalBags, null);
            Arrays.fill(passengerCollectedBags, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            print();
            lock.unlock();
        }
    }

    void print() {
        List<Integer> tempWaitingQueue = new LinkedList<>(waitingQueue);
        String stat = String.format("" +
                " '%1d'  '%1d'  '%3.3s'  '%1d'  '%1d'   '%3.3s'   '%1d'  '%1d'  '%1d'  '%1d'  '%1d'  '%1d'   '%1d'  '%1d'  '%1d'" +
                "\n" +
                "'%3.3s' '%3.s'  '%1d'   '%1d'  '%3.3s' '%3.3s'  '%1d'   '%1d'  '%3.3s' '%3.3s'  '%1d'   '%1d'  '%3.3s' '%3.3s  '%1d'   '%1d'  '%3.3s' '%3.3s'  '%1d'   '%1d'  '%3.3s' '%3.3s'  '%1d'   '%1d'" +
                "\n",
                FN, BN,
                mapPorterStat(porterStat), CB, SR,
                mapBusDriverStat(busDriverStat), oneHifen(tempWaitingQueue.get(0)), oneHifen(tempWaitingQueue.get(1)), oneHifen(tempWaitingQueue.get(2)), oneHifen(tempWaitingQueue.get(3)), oneHifen(tempWaitingQueue.get(4)), oneHifen(tempWaitingQueue.get(5)),
                oneHifen(busSeats[0]), oneHifen(busSeats[1]), oneHifen(busSeats[2]),
                mapPassenger(passengerStat[0]), mapTransit(transit[0]), oneHifen(startTotalBags[0]), oneHifen(passengerCollectedBags[0]),
                mapPassenger(passengerStat[1]), mapTransit(transit[1]), oneHifen(startTotalBags[1]), oneHifen(passengerCollectedBags[1]),
                mapPassenger(passengerStat[2]), mapTransit(transit[2]), oneHifen(startTotalBags[2]), oneHifen(passengerCollectedBags[2]),
                mapPassenger(passengerStat[3]), mapTransit(transit[3]), oneHifen(startTotalBags[3]), oneHifen(passengerCollectedBags[3]),
                mapPassenger(passengerStat[4]), mapTransit(transit[4]), oneHifen(startTotalBags[4]), oneHifen(passengerCollectedBags[4]),
                mapPassenger(passengerStat[5]), mapTransit(transit[5]), oneHifen(startTotalBags[5]), oneHifen(passengerCollectedBags[5])
                );
        System.out.println(stat);
        log.add(stat);
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


    String mapPorterStat(SPorter stat){
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

    String mapBusDriverStat(SBusDriver stat){
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

    String mapPassenger(SPassenger stat) {
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
}
