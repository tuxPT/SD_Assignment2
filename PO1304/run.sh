#!/usr/bin/env sh

# serverSide
cd bin

java serverSide.GeneralRepository.mainGeneralRepository &
sleep 1

java serverSide.ArrivalLounge.mainArrivalLounge &
java serverSide.ArrivalTerminalExit.mainArrivalTerminalExit &
java serverSide.ArrivalTerminalTransferQuay.mainArrivalTerminalTransferQuay &
java serverSide.BaggageCollectionPoint.mainBaggageCollectionPoint &
java serverSide.BaggageReclaimOffice.mainBaggageReclaimOffice &
java serverSide.DepartureTerminal.mainDepartureTerminal &
java serverSide.DepartureTerminalTransferQuay.mainDepartureTerminalTransferQuay &
java serverSide.TemporaryStorageArea.mainTemporaryStorageArea &

sleep 1

# clientSide

java clientSide.BusDriver.mainBusDriver &
java clientSide.Porter.mainPorter &
java clientSide.Passenger.mainPassenger &
