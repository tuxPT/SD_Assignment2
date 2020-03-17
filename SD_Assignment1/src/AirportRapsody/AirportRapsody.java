package AirportRapsody;

import AirportRapsody.Interface.*;
import AirportRapsody.Monitor.*;
import AirportRapsody.Thread.TBusDriver;
import AirportRapsody.Thread.TPassenger;
import AirportRapsody.Thread.TPorter;

public class AirportRapsody {
    public static void main(String[] args) throws Exception {
        Integer PLANE_PASSENGERS, MAX_PORTER, MAX_BUSDRIVER, PLANES_PER_DAY, MAX_BAGS_NUMBER;
        PLANE_PASSENGERS = 6;
        MAX_PORTER = 1;
        MAX_BUSDRIVER = 1;
        PLANES_PER_DAY = 5;
        Boolean END_OF_DAY = false;
        MAX_BAGS_NUMBER = 2;

        // Arrays de threads
        TPorter[] TPorter = new TPorter[MAX_PORTER];
        TBusDriver[] TBusDriver = new TBusDriver[MAX_BUSDRIVER];
        TPassenger[] TPassenger = new TPassenger[PLANE_PASSENGERS];

        // INSTANCIAR MONITORES
        MLogger MLogger = new MLogger();
        MArrivalLounge MArrivalLounge = new MArrivalLounge(PLANE_PASSENGERS);
        MArrivalTerminalExit MArrivalTerminalExit = new MArrivalTerminalExit();
        MArrivalTerminalTransferQuay MArrivalTerminalTransferQuay = new MArrivalTerminalTransferQuay();
        MBaggageCollectionPoint MBaggageCollectionPoint = new MBaggageCollectionPoint();
        MBaggageReclaimOffice MBaggageReclaimOffice = new MBaggageReclaimOffice();
        MDepartureTerminal MDepartureTerminal = new MDepartureTerminal();
        MDepartureTerminalTransferQuay MDepartureTerminalTransferQuay = new MDepartureTerminalTransferQuay();
        MTemporaryStorageArea MTemporaryStorageArea = new MTemporaryStorageArea();

        //Instanciar threads

        for (int i=0; i<MAX_BUSDRIVER; i++) {
            TBusDriver[i] = new TBusDriver(i,
                    (IArrivalTerminalTransferQuayBusDriver) MArrivalTerminalTransferQuay,
                    (IDepartureTerminalTransferQuayBusDriver) MDepartureTerminalTransferQuay);
            //executa o run
            TBusDriver[i].start();
        }

        for (int i=0; i<MAX_PORTER; i++) {
            TPorter[i] = new TPorter(i,
                    (IArrivalLoungePorter) MArrivalLounge,
                    (IBaggageCollectionPointPorter) MBaggageCollectionPoint,
                    (ITemporaryStorageAreaPorter) MTemporaryStorageArea
            );
            //executa o run
            TPorter[i].start();
        }
        Integer[] bags = new Integer[PLANE_PASSENGERS*MAX_BAGS_NUMBER];
        for (int i=0; i<PLANE_PASSENGERS; i++) {

            TPassenger[i] = new TPassenger(i, MAX_BAGS_NUMBER,
                    (IArrivalLoungePorter) MArrivalLounge,
                    (IArrivalTerminalExitPassenger) MArrivalTerminalExit,
                    (IArrivalTerminalTransferQuayPassenger) MArrivalTerminalTransferQuay,
                    (IBaggageCollectionPointPassenger) MBaggageCollectionPoint,
                    (IBaggageReclaimOfficePassenger) MBaggageReclaimOffice,
                    (IDepartureTerminalTransferQuayPassenger) MDepartureTerminalTransferQuay
            );
            //executa o run
            TPassenger[i].start();
        }

        //Wait for joins
        for(int i=0;i<TPassenger.length; i++){
            try{
                TPassenger[i].join();
            } catch(InterruptedException e){}
        }
        for(int i=0;i<TPorter.length;i++){
            try{
                TPorter[i].join();
            } catch(InterruptedException e){}
        }
        for(int i=0;i<TBusDriver.length;i++){
            try{
                TBusDriver[i].join();
            } catch(InterruptedException e){}
        }
        //MLogger.close();
    }

}