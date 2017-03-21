package jammission.world;

import java.util.ArrayList;
import java.util.List;

public class Vision extends LocationObject {
    protected List bearwifes;
    protected List bears;
    protected List lairs;
    protected List bushes;
    protected River river;
    protected boolean daytime;

    public Vision() {
        this.bearwifes = new ArrayList();
        this.bears = new ArrayList();
        this.lairs = new ArrayList();
        this.bushes = new ArrayList();
        this.river = new River();
    }

    public Vision(List bearwifes, List bears, List lairs, List bushes, River river, boolean daytime) {
        this.bearwifes = bearwifes;
        this.bears = bears;
        this.lairs = lairs;
        this.bushes = bushes;
        this.daytime = daytime;
        this.river = river;
        setId("");
    }

    public Bearwife[] getBearwifes() {
        return (Bearwife[])bearwifes.toArray(new Bearwife[bearwifes.size()]);
    }
    public Bearwife getBearwife(int idx) {
        return (Bearwife)this.bearwifes.get(idx);
    }
    public void setBearwifes(Bearwife[] bearwifes) {
        this.bearwifes.clear();
        for(int i = 0; i < bearwifes.length; i++)
            this.bearwifes.add(bearwifes[i]);
    }
    public void setBearwife(int idx, Bearwife bearwife) {
        this.bearwifes.set(idx, bearwife);
    }
    public void addBearwife(Bearwife bearwife) {
        this.bearwifes.add(bearwife);
    }
    public boolean removeBearwife(Bearwife bearwife) {
        return this.bearwifes.remove(bearwife);
    }


    public Bear[] getBears() {
        return (Bear[])bears.toArray(new Bear[bears.size()]);
    }
    public Bear getBear(int idx) {
        return (Bear)this.bears.get(idx);
    }
    public void setBears(Bear[] bears) {
        this.bears.clear();
        for(int i = 0; i < bears.length; i++)
            this.bears.add(bears[i]);
    }
    public void setBear(int idx, Bear bear) {
        this.bears.set(idx, bear);
    }
    public void addBear(Bear bear) {
        this.bears.add(bear);
    }
    public boolean removeBear(Bear bear) {
        return this.bears.remove(bear);
    }


    public Lair[] getLairs() {
        return (Lair[])lairs.toArray(new Lair[lairs.size()]);
    }
    public Lair getLair(int idx) {
        return (Lair)this.lairs.get(idx);
    }
    public void setLairs(Lair[] lairs) {
        this.lairs.clear();
        for(int i = 0; i < lairs.length; i++)
            this.lairs.add(lairs[i]);
    }
    public void setLair(int idx, Lair lair) {
        this.lairs.set(idx, lair);
    }
    public void addLair(Lair lair) {
        this.lairs.add(lair);
    }
    public boolean removeLair(Lair lair) {
        return this.lairs.remove(lair);
    }


    public Bush[] getBushes() {
        return (Bush[])bushes.toArray(new Bush[bushes.size()]);
    }
    public Bush getBush(int idx) {
        return (Bush)this.bushes.get(idx);
    }
    public void setBushes(Bush[] bushes) {
        this.lairs.clear();
        for(int i = 0; i < lairs.length; i++)
            this.lairs.add(lairs[i]);
    }
    public void setBush(int idx, Bush bush) {
        this.bushes.set(idx, bush);
    }
    public void addBush(Bush bush) {
        this.bushes.add(bush);
    }
    public boolean removeBush(Bush bush) {
        return this.bushes.remove(bush);
    }


    public River getRiver() {
        return river;
    }
    public void setRiver(River river) {
        this.river = river;
    }


    public boolean isDaytime() {
        return this.daytime;
    }
    public void setDaytime(boolean daytime) {
        this.daytime = daytime;
    }

    public String toString() {
        return "Vision(" + "id=" + getId() + ", location=" + getLocation() + ")";
    }

    public Object clone() {
        Vision clone = (Vision)super.clone();
        clone.bearwifes = (ArrayList)((ArrayList)this.bearwifes).clone();
        clone.bears = (ArrayList)((ArrayList)this.bears).clone();
        clone.lairs = (ArrayList)((ArrayList)this.lairs).clone();
        clone.bushes = (ArrayList)((ArrayList)this.bushes).clone();
        clone.river = this.river.clone();
        return clone;
    }
}
