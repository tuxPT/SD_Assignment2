package AirportRapsody.Monitor;

import AirportRapsody.Interface.IArrivalLoungePassenger;
import AirportRapsody.Interface.IArrivalLoungePorter;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MArrivalLounge implements IArrivalLoungePassenger, IArrivalLoungePorter {
    private Integer NUMBER_OF_PASSENGERS;
    private Integer PLANE_PASSENGERS;

    ReentrantLock lock = new ReentrantLock();
    Condition whatShouldIDo = lock.newCondition();

    public MArrivalLounge(int PLANE_PASSENGERS)
    {
        NUMBER_OF_PASSENGERS = 0;
    }


    // PASSENGER
    public void addPassenger()
    {

        NUMBER_OF_PASSENGERS++;
        if(NUMBER_OF_PASSENGERS == PLANE_PASSENGERS){
            whatShouldIDo.signalAll();
        }
    }

    // PASSENGER
    private void RemovePassenger()
    {
        NUMBER_OF_PASSENGERS--;
    }

    @Override
    public void removeBag() {
        while(NUMBER_OF_PASSENGERS != PLANE_PASSENGERS){
            try {
                whatShouldIDo.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //remover mala

    }
}