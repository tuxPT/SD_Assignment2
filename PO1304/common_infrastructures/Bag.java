package common_infrastructures;

import java.io.Serializable;

public class Bag implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -1494835338276539334L;
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