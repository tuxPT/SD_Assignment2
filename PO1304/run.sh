#!/bin/env sh

# serverSide
cd bin

java serverSide.GeneralRepository.mainGeneralRepository > GeneralRepository &
sleep 10

java serverSide.ArrivalLounge.mainArrivalLounge > ArrivalLounge &
java serverSide.ArrivalTerminalExit.mainArrivalTerminalExit > ArrivalTerminalExit &
java serverSide.ArrivalTerminalTransferQuay.mainArrivalTerminalTransferQuay > ArrivalTerminalTransferQuay &
java serverSide.BaggageCollectionPoint.mainBaggageCollectionPoint > BaggageCollectionPoint &
java serverSide.BaggageReclaimOffice.mainBaggageReclaimOffice > BaggageReclaimOffice &
java serverSide.DepartureTerminal.mainDepartureTerminal > DepartureTerminal &
java serverSide.DepartureTerminalTransferQuay.mainDepartureTerminalTransferQuay > DepartureTerminalTransferQuay &
java serverSide.TemporaryStorageArea.mainTemporaryStorageArea > TemporaryStorageArea &

sleep 10

# clientSide

java clientSide.BusDriver.mainBusDriver > BusDriver &
java clientSide.Porter.mainPorter > Porter &
java clientSide.Passenger.mainPassenger > Passenger &