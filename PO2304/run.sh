#!/usr/bin/env sh

# Ports Assignment
AL_PORT="21010"
AL_HOST="localhost"
ATE_PORT="21020"
ATE_HOST="localhost"
ATTQ_PORT="21030"
ATTQ_HOST="localhost"
BCP_PORT="21040"
BCP_HOST="localhost"
BRO_PORT="21050"
BRO_HOST="localhost"
DT_PORT="21060"
DT_HOST="localhost"
DTTQ_PORT="21070"
DTTQ_HOST="localhost"
TSA_PORT="21080"
TSA_HOST="localhost"
GR_PORT="21090"
GR_HOST="localhost"

# serverSide
cd bin

java serverSide.GeneralRepository.mainGeneralRepository $GR_PORT  &
sleep 1

java serverSide.ArrivalLounge.mainArrivalLounge $AL_PORT $GR_HOST $GR_PORT  &
java serverSide.ArrivalTerminalExit.mainArrivalTerminalExit $ATE_PORT $GR_HOST $GR_PORT &
java serverSide.ArrivalTerminalTransferQuay.mainArrivalTerminalTransferQuay $ATTQ_PORT $GR_HOST $GR_PORT &
java serverSide.BaggageCollectionPoint.mainBaggageCollectionPoint $BCP_PORT $GR_HOST $GR_PORT &
java serverSide.BaggageReclaimOffice.mainBaggageReclaimOffice $BRO_PORT $GR_HOST $GR_PORT &
java serverSide.DepartureTerminal.mainDepartureTerminal $DT_PORT $GR_HOST $GR_PORT &
java serverSide.DepartureTerminalTransferQuay.mainDepartureTerminalTransferQuay $DTTQ_PORT $GR_HOST $GR_PORT &
java serverSide.TemporaryStorageArea.mainTemporaryStorageArea $TSA_PORT $GR_HOST $GR_PORT &


sleep 1

# clientSide

java clientSide.BusDriver.mainBusDriver $ATTQ_HOST $ATTQ_PORT $DTTQ_HOST $DTTQ_PORT &
java clientSide.Porter.mainPorter $AL_HOST $AL_PORT $BCP_HOST $BCP_PORT $TSA_HOST $TSA_PORT &
java clientSide.Passenger.mainPassenger $AL_HOST $AL_PORT $ATE_HOST $ATE_PORT $ATTQ_HOST $ATTQ_PORT $BCP_HOST $BCP_PORT $BRO_HOST $BRO_PORT $DT_HOST $DT_PORT $DTTQ_HOST $DTTQ_PORT $GR_HOST $GR_PORT &

sleep 1

for job in `jobs -p`
do
    wait $job
done

sleep 15

exit 0
