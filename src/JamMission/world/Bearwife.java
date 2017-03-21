package jammission.world;

import java.util.ArrayList;
import java.util.List;

public class Bearwife extends LocationObject {

    protected int watervolume;
    protected int bottlecapacity;
    protected double visionrange;
    protected String name;

    public Bearwife() {}

    public Bearwife(Location location, String name, int volume, int capacity, double vision) {
        setLocation(location);
        setId(name);
        setName(name);
        setVisionRange(vision);
        setWaterVolume(volume);
        setBottleCapacity(capacity)
    }

    public int getWaterVolume() {
        return this.watervolume;
    }
    public void setWaterVolume(int volume) {
        int old = watervolume;
        watervolume = volume;
        pcs.firePropertyChange("watervolume", old, watervolume);
    }


    public int getBottleCapacity() {
        return this.bottlecapacity;
    }
    public void setBottleCapacity(int capacity) {
        int old = bottlecapacity;
        bottlecapacity = capacity;
        pcs.firePropertyChange("bottlecapacity", old, bottlecapacity);
    }


    public double getVisionRange() {
        return this.visionrange;
    }
    public void setVisionRange(double visionrange) {
        double old = this.visionrange;
        this.visionrange = visionrange;
        pcs.firePropertyChange("visionrange", old, this.visionrange);
    }


    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        String old = this.name;
        this.name = name;
        pcs.firePropertyChange("name", old, this.name);
    }

    public void update(Bearwife br) {
        assert this.getId().equals(br.getId());

        setWaterVolume(br.getWaterVolume);
        setBottleCapacity(br.getBottleCapacity);
        setVisionRange(br.getVisionRange());
    }

    public String toString() {
        return "Bearwife(" + "id=" + getId() + ", location=" + getLocation() + ", name=" + getName() + ")";
    }

    public boolean isBottleFull() {
        return (watervolume >= bottlecapacity);
    }


    public Object clone() {
        Bearwife clone = (Bearwife)super.clone();
        return clone;
    }
}
