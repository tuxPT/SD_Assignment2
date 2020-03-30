package AirportRapsody.Interface;

import AirportRapsody.State.SPassenger;

public interface IBaggageReclaimOfficePassenger {
    SPassenger addBag(Integer id, int i);
    SPassenger goHome(Integer id);
}
