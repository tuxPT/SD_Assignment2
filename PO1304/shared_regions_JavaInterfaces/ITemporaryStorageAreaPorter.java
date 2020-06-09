package shared_regions_JavaInterfaces;

import common_infrastructures.SPorter;

public interface ITemporaryStorageAreaPorter {
    /**
     * Called by the porter.<br>
     * Adds one more bags to the total of bags currently present in the storeroom.
     @return Porter's state AT_THE_PLANES_HOLD
     @see SPorter
     */
    SPorter addBag();
}
