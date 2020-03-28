package AirportRapsody.Monitor;

public class Bag{
    private Integer ID;
    private Boolean TRANSIT;

    public Integer getID() {
        return ID;
    }

    public Boolean getTRANSIT() {
        return TRANSIT;
    }

    public Bag(Integer ID, Boolean TRANSIT) {
        this.ID = ID;
        this.TRANSIT = TRANSIT;
    }

    public boolean equals(Bag other) 
    {
        return this.ID == other.ID;
    }
}