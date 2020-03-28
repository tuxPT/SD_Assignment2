package AirportRapsody.Interface;

import java.util.List;

import AirportRapsody.State.SPassenger;

public interface IArrivalLoungePassenger {
    SPassenger whatShouldIDo(List<Integer> t_bags, boolean t_TRANSIT);
}
