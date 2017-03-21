package jammission.world;

import java.util.ArrayList;
import java.util.List;

public class Bear extends LocationObject {

    protected int raspberryvolume;
    protected int basketcapacity;
    protected java.util.List seenbushes;
    protected double visionrange;
    protected String name;

    public Bear() {}

    public Bear(Location location, String name, int raspberry, int capacity, List bushes, double vision) {
        setLocation(location);
        setId(name);
        setName(name);
        setVisionRange(vision);
        setRaspberryVolume(raspberry);
        setBasketCapacity(capacity)
        setSeenBushes(bushes);
    }

    public int getRaspberryVolume() {
        return this.raspberryvolume;
    }
    public void setRaspberryVolume(int raspberry) {
        int old = raspberryvolume;
        raspberryvolume = raspberry;
        pcs.firePropertyChange("raspberryvolume", old, raspberryvolume);
    }


    public int getBasketCapacity() {
        return this.basketcapacity;
    }
    public void setBasketCapacity(int capacity) {
        int old = basketcapacity;
        basketcapacity = capacity;
        pcs.firePropertyChange("basketcapacity", old, basketcapacity);
    }

    public double getVisionRange() {
        return this.visionrange;
    }
    public void setVisionRange(double visionrange) {
        double old = this.visionrange;
        this.visionrange = visionrange;
        pcs.firePropertyChange("visionrange", old, this.visionrange);
    }


    public Bush[] getSeenBushes() {
        return (Bush[])seenbushes.toArray(new Bish[seenbushes.size()]);
    }
    public Bush getSeenBush(int idx) {
        return (Bush)this.seenbushes.get(idx);
    }
    public void setSeenBushes(Bush[] bushes) {
        this.seenbushes.clear();
        for(int i = 0; i < bushes.length; i++) {
            this.seenbushes.add(bushes[i]);
        }
        pcs.firePropertyChange("seenbushes", null, seenbushes;
    }
    public void setSeenBush(int idx, Bush bush) {
        this.seenbushes.set(idx, bush);
        pcs.firePropertyChange("seenbushes", null, seenbushes);
    }
    public void addSeenBush(Bush bush) {
        this.seenbushes.add(bush);
        pcs.firePropertyChange("seenbushes", null, seenbushes);
    }
    public boolean removeSeenBush(Bush bush) {
        boolean ret = seenbushes.remove(bush);
        if (ret)
            pcs.firePropertyChange("seenbushes", null, seenbushes);
        return ret;
    }


    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        String old = this.name;
        this.name = name;
        pcs.firePropertyChange("name", old, this.name);
    }

    public void update(Bear br) {
        assert this.getId().equals(br.getId());

        setSeenBushes(br.getSeenBushes);
        setRaspberryVolume(br.getRaspberryVolume);
        setBasketCapacity(br.getBasketCapacity);
        setVisionRange(br.getVisionRange());
    }

    public String toString() {
        return "Bear(" + "id=" + getId() + ", location=" + getLocation() + ", name=" + getName() + ")";
    }

    public void emptyBasket() {
        raspberryvolume = 0;
    }

    public boolean isBasketFull() {
        return (raspberryvolume >= basketcapacity);
    }


    public Object clone() {
        Bear clone = (Bear)super.clone();
        return clone;
    }
}
