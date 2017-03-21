package jammission.world;


public class Bush extends LocationObject {

    protected static int bushcnt;
    protected int raspberryamount;

    protected static synchronized int getNumber() {
        return ++bushcnt;
    }

    public Bush() {}

    public Bush(Location location, int amount) {
        this("Bush #"+getNumber(), location, amount);
    }

    public Bush(String id, Location location, int amount) {
        setId(id);
        setLocation(location);
        setRaspberryAmount(amount);
    }

    public int getRaspberryAmount() {
        return this.raspberryamount;
    }

    public void setRaspberryAmount(int amount) {
        int old = raspberryamount;
        raspberryamount = amount;
        pcs.firePropertyChange("raspberryamount", old, raspberryamount);
    }

    public String toString() {
        return "Bush(" + "id="+getId() + ", location="+getLocation() + ")";
    }
    public boolean isEmpty() {
        return (raspberryamount == 0);
    }
}

