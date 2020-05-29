package shared_regions;

import common_infrastructures.Bag;
import common_infrastructures.SPorter;

public interface IArrivalLoungePorter {
    /**
     * Called by the porter when it reaches the plane hold.<br>
     * In the case the plane hold is not empty, it'll return the bag in index zero.<br>
     * @return the bag that was collected or null if the plane hold is empty.
     * @see Bag
     */
    Bag tryToCollectABag();

    /**
     * Called by the porter while is waiting for a plane to land.<br>
     * Checks if all passengers have disembarked from the plane.<br>
     * If they did, returns the AT_THE_PLANES_HOLD state, otherwise it'll return the present state (WAITING_FOR_A_PLANE_TO_LAND).<br>
     * @return Porter's state AT_THE_PLANES_HOLD or WAITING_FOR_A_PLANE_TO_LAND
     * @see SPorter
     */
    boolean takeARest();

    /**
     * Called by the porter when he goes to the plane's hold and there are none left.<br>
     * Resets the number of passengers.
     * @return Porter's state WAITING_FOR_A_PLANE_TO_LAND
     * @see SPorter
     */
    SPorter noMoreBagsToCollect();

    /**
     * Called by the porter after it picks a bag.     <br>
     * It chooses the next state depending on the bag's type.<br>
     * If the bag belongs to a passenger having the current airport as the final destination it'll return the conveyor's belt state,
     * if it's from a passenger that is in transit, it'll return the storeroom's state.
     * @param bag the current bag that the porter is carrying.
     * @return Porter's state AT_THE_STOREROOM or AT_THE_LUGGAGE_BELT_CONVEYOR
     * @see SPorter
     */
    SPorter carryItToAppropriateStore(Bag bag);

}
