package AirportRapsody.Monitor;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import AirportRapsody.Interface.IBaggageCollectionPointPassenger;
import AirportRapsody.Interface.IBaggageCollectionPointPorter;
import AirportRapsody.Interface.IGeneralRepository;
import AirportRapsody.State.SPassenger;
import AirportRapsody.State.SPorter;

public class MBaggageCollectionPoint implements IBaggageCollectionPointPorter, IBaggageCollectionPointPassenger {
    private IGeneralRepository MGeneralRepository;
    ReentrantLock lock = new ReentrantLock(true);
    Condition porterArrival = lock.newCondition();
    private boolean moreBags;      

    List<Bag> ListOfBags;

    public MBaggageCollectionPoint(MGeneralRepository MGeneralRepository)
    {
        ListOfBags = new ArrayList<>(); 
        this.moreBags = true;
        this.MGeneralRepository = MGeneralRepository;
    }

     // PASSENGER
     public Integer goCollectABag(Integer id, List<Integer> total_bags)
     {          
        lock.lock();
        Integer NUMBER_OF_BAGS_RETRIEVED = 0;
         try{
             while(moreBags) {
                 porterArrival.await();

             //System.out.println("PASSAGEIROS ACORDARAM");
                List<Bag> removeList = new LinkedList<>();
                for (Bag bag: ListOfBags){
                    for (Integer i: total_bags){
                        // BAG BELONGS TO PASSENGER
                        if (i == bag.getID()){
                            //System.out.println("PASSAGEIRO RETIROU UMA MALA");
                            removeList.add(bag);
                            NUMBER_OF_BAGS_RETRIEVED++;
                            MGeneralRepository.updatePassenger(null, id, null, null, null, true, null);
                            //MGeneralRepository.updatePorter(null, null, ListOfBags.size(), null);
                        }
                    }   
                }
                ListOfBags.removeAll(removeList);     
                MGeneralRepository.updatePorter(null, null, ListOfBags.size(), null);           
                if (total_bags.size() == NUMBER_OF_BAGS_RETRIEVED)
                {
                    return NUMBER_OF_BAGS_RETRIEVED;
                }
                //porterArrival.await();
             }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
        return NUMBER_OF_BAGS_RETRIEVED;
     }     

    // PORTER

    @Override
    public SPorter addBag(Bag bag) {
        lock.lock();
        try{
            ListOfBags.add(bag);
            porterArrival.signalAll();   
            //System.out.println("COLOCOU UMA MALA");         
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{           
            MGeneralRepository.updatePorter(SPorter.AT_THE_PLANES_HOLD, null, ListOfBags.size(), null);
            lock.unlock();
        }
        return SPorter.AT_THE_PLANES_HOLD;
    }

    public void warnPassengers(){
        lock.lock();
        try{
            this.moreBags = false;
            porterArrival.signalAll();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public void newPlane(){
        lock.lock();
        try{
            this.moreBags = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}
