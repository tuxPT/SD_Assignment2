package shared_regions;

import java.util.List;

public interface IBaggageCollectionPointPassenger {
    /**
     * Called by a passenger.<br>
     * Simulates a passenger waiting in the conveyor's belt for its bags.<br>
     * Each time the porter puts a bag in the belt, he'll check if it's his.<br>
     * If it is, it'll remove it from the conveyor's belt.<br>
     * The passengers will stay here until they have all of their bags or until the porter warns them that there are no more bags left
     * (this means the passenger lost some of its bags).
     @param id passenger's id
     @param t the list of the bag's ID that the passenger had at the beggining of the journey
     @return The number of bags that the passenger collected
     */
    Integer goCollectABag(Integer id, List<Integer> t);
}
