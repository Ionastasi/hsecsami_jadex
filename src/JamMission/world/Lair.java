package jammission.world;

import java.util.ArrayList;
import java.util.List;

public class Lair extends LocationObject {

    protected static int laircnt;
    protected int watervolume;
    protected int raspberryvolume;
    protected int jamvolume;
    protected String name;

    protected static synchronized int getNumber() {
        return ++bushcnt;
    }

    public Lair() {}

    public Lair(Location location) {
        this(location, "Lair #"+getNumber(), 0, 0, 0);
    }

    public Lair(Location location, String name, int water, int raspberry, int jam) {
        setLocation(location);
        setId(name);
        setName(name);
        setWaterVolume(water);
        setRaspberryVolume(raspberry);
        setJamVolume(jam);
    }

    public int getWaterVolume() {
        return this.watervolume;
    }
    public void setWaterVolume(int volume) {
        int old = watervolume;
        watervolume = volume;
        pcs.firePropertyChange("watervolume", old, watervolume);
    }
    public void increaseWaterVolume(int volume) {
        int old = watervolume;
        watervolume += volume;
        pcs.firePropertyChange("watervolume", old, watervolume);
    }

    public int getRaspberryVolume() {
        return this.raspberryvolume;
    }
    public void setRaspberryVolume(int volume) {
        int old = raspberryvolume;
        raspberryvolume = volume;
        pcs.firePropertyChange("raspberryvolume", old, raspberryvolume);
    }
    public void increaseRaspberryVolume(int volume) {
        int old = raspberryvolume;
        raspberryvolume += volume;
        pcs.firePropertyChange("raspberryvolume", old, raspberryrvolume);
    }

    public int getJamVolume() {
        return this.jamvolume;
    }
    public void setJamVolume(int volume) {
        int old = jamvolume;
        jamvolume = volume;
        pcs.firePropertyChange("jamvolume", old, jamvolume);
    }
    public void increaseJamVolume(int volume) {
        int old = jamrvolume;
        jamvolume += volume;
        pcs.firePropertyChange("jamvolume", old, jamvolume);
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        String old = this.name;
        this.name = name;
        pcs.firePropertyChange("name", old, this.name);
    }

    public void update(Lair lr) {
        assert this.getId().equals(lr.getId());

        setWaterVolume(lr.getWaterVolume);
        setRaspberryVolume(lr.getRaspberryVolume);
        setJamVolume(lr.getJamVolume);
        }

    public String toString() {
        return "Lair(" + "id=" + getId() + ", location=" + getLocation() + ", name=" + getName() + ")";
    }

    public Object clone() {
        Lair clone = (Lair)super.clone();
        return clone;
    }
}
