package shared_regions_JavaInterfaces;

import common_infrastructures.Bag;
import common_infrastructures.SPorter;

public interface IBaggageCollectionPointPorter {
    /**
     * Called by the porter.<br>
     * Porter warns the passengers that there are no more bags left.
     */
    void warnPassengers();

    /**
     * Called by the porter.<br>
     * Porter will add a bag to the conveyor's belt and warn the passengers so they can check if the bag belongs to them.
     * @param bag bag to be added to the conveyor's belt
     * @return Porter's state AT_THE_PLANES_HOLD
     * @see SPorter
     */
    SPorter addBag(Bag bag);
}
