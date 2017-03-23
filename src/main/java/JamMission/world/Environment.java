package jammission.world;

import java.util.ArrayList;
import java.util.List;

import jadex.commons.SimplePropertyChangeSupport;
import jadex.commons.beans.PropertyChangeListener;

public class Environment implements IEnvironment {

    /** The singleton. */
    protected static Environment instance;
    /** The helper object for bean events. */
    public SimplePropertyChangeSupport pcs;

    protected boolean daytime;
    protected List bears;
    protected List bearwifes;
    protected List lairs;
    protected List bushes;
    protected River river;

    public Environment() {
        this.pcs = new SimplePropertyChangeSupport(this);

        this.daytime = true;
        this.bears = new ArrayList();
        this.bearwifes = new ArrayList();
        this.lairs = new ArrayList();
        this.bushes = new ArrayList();
        this.river = new River(new Location(0.5, 0.5));

        // Add some things to our world.
        addLair(new Lair(new Location(0.1, 0.1)));
        addBush(new Bush(new Location(0.1, 0.5), 10));
        addBush(new Bush(new Location(0.1, 0.6), 20));
        addBush(new Bush(new Location(0.7, 0.1), 5));
        addBush(new Bush(new Location(0.9, 0.9), 15));
        addBush(new Bush(new Location(0.6, 0.8), 12));
    }

    public static synchronized Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

    public synchronized Vision getVision(LocationObject o) {
        Location loc;
        double range;
        // beacuse compiler is stupid
        if (o instanceof Bear) {
            Bear br = (Bear)o;
            loc = br.getLocation();
            range = br.getVisionRange();
        } else if (o instanceof Bearwife) {
            Bearwife br = (Bearwife)o;
            loc = br.getLocation();
            range = br.getVisionRange();
        } else {
            return null;
        }

        ArrayList nearbears = new ArrayList();
        for(int i=0; i<bears.size(); i++) {
            LocationObject  obj = (LocationObject)bears.get(i);
            if(obj.getLocation().isNear(loc, range))
                nearbears.add(obj.clone());
        }

        ArrayList nearbearwifes = new ArrayList();
        for(int i=0; i<bearwifes.size(); i++) {
            LocationObject  obj = (LocationObject)bearwifes.get(i);
            if(obj.getLocation().isNear(loc, range))
                nearbearwifes.add(obj.clone());
        }

        ArrayList nearlairs = new ArrayList();
        for(int i=0; i<lairs.size(); i++) {
            LocationObject  obj = (LocationObject)lairs.get(i);
            if(obj.getLocation().isNear(loc, range))
                nearlairs.add(obj.clone());
        }

        ArrayList nearbushes = new ArrayList();
        for(int i=0; i<bushes.size(); i++) {
            LocationObject  obj = (LocationObject)bushes.get(i);
            if(obj.getLocation().isNear(loc, range))
                nearbushes.add(obj.clone());
        }
        River nearriver = null;
        if (river.getLocation().isNear(loc, range)) {
            nearriver = (River) river.clone();
        }
        Vision v = new Vision(nearbearwifes, nearbears, nearlairs,
                              nearbushes, nearriver, getDaytime());

        return v;
    }

    //-------- Bear actions --------
    public boolean harvestRaspberry(Bear bear, Bush bush) {
        bear = getBear(bear);
        bush = getBush(bush);
        if (bear == null || bush == null) {
            return false;
        }
        int have = bear.getRaspberryVolume();
        int lim = bear.getBasketCapacity();
        int raspberry = bush.getRaspberryAmount();
        if (raspberry < lim - have) {
            bear.setRaspberryVolume(have + raspberry);
            bush.setRaspberryAmount(0);
        } else {
            bear.setRaspberryVolume(lim);
            bush.setRaspberryAmount(raspberry - (lim - have));
        }
        return true;
    }

    public boolean giveAwayRaspberry(Bear bear, Lair lair) {
        bear = getBear(bear);
        lair = getLair(lair);
        if (bear == null || lairs == null) {
            return false;
        }
        lair.increaseRaspberryVolume(bear.getRaspberryVolume());
        bear.setRaspberryVolume(0);
        return true;
    }

    //-------- Bearwife actions --------
    public boolean getWater(Bearwife bearwife) {
        bearwife = getBearwife(bearwife);
        if (bearwife == null) {
            return false;
        }
        bearwife.setWaterVolume(bearwife.getBottleCapacity());
        return true;
    }

    public boolean pourWater(Bearwife bearwife, Lair lair) {
        bearwife = getBearwife(bearwife);
        lair = getLair(lair);
        if (bearwife == null || lair == null) {
            return false;
        }
        lair.increaseWaterVolume(bearwife.getWaterVolume());
        bearwife.setWaterVolume(0);
        return true;
    }

    public boolean makeJam(Lair lair) {
        //some kind of timer?
        lair = getLair(lair);
        if (lair == null || lair.getWaterVolume() == 0 || lair.getRaspberryVolume() == 0) {
            return false;
        }
        int raspberry = lair.getRaspberryVolume();
        int water = lair.getWaterVolume();
        if (raspberry >= 2 * water) {
            lair.increaseJamVolume(water);
            lair.setWaterVolume(0);
            lair.increaseRaspberryVolume(-1 * 2 * water);
        } else {
            lair.increaseJamVolume(raspberry / 2);
            lair.setRaspberryVolume(raspberry % 2);
            lair.increaseWaterVolume(-1 * raspberry / 2);
        }
        return true;
    }


    protected synchronized Bear getBear(Bear bear) {
        for(int i=0; i<bears.size(); i++) {
            if(bear.equals(bears.get(i))) {
                return (Bear)bears.get(i);
            }
        }
        return null;
    }
    protected synchronized Bearwife getBearwife(Bearwife bearwife) {
        for(int i=0; i<bearwifes.size(); i++) {
            if(bearwife.equals(bearwifes.get(i))) {
                return (Bearwife)bearwifes.get(i);
            }
        }
        return null;
    }
    protected synchronized Lair getLair(Lair lair) {
        for(int i=0; i<lairs.size(); i++) {
            if(lair.equals(lairs.get(i))) {
                return (Lair)lairs.get(i);
            }
        }
        return null;
    }
    protected synchronized Bush getBush(Bush bush) {
        for(int i=0; i<bushes.size(); i++) {
            if(bush.equals(bushes.get(i))) {
                return (Bush)bushes.get(i);
            }
        }
        return null;
    }

    //-------- methods --------
    public synchronized Vision getCompleteVision() {
        return new Vision(bearwifes, bears, lairs, bushes, river, getDaytime());
    }

    public synchronized boolean getDaytime() {
        return daytime;
    }

    public synchronized void setDaytime(boolean daytime) {
        this.daytime = daytime;
        this.pcs.firePropertyChange("daytime", null, Boolean.valueOf(daytime));
    }

    public synchronized void addBear(Bear bear) {
        bears.add(bear);
        this.pcs.firePropertyChange("bears", null, bear);
    }

    public synchronized void removeBear(Bear bear) {
        bears.remove(bear);
        this.pcs.firePropertyChange("bears", null, bear);
    }

    public synchronized void addBearwife(Bearwife bearwife) {
        bearwifes.add(bearwife);
        this.pcs.firePropertyChange("bearwifes", null, bearwife);
    }

    public synchronized void removeBearwife(Bearwife bearwife) {
        bearwifes.remove(bearwife);
        this.pcs.firePropertyChange("bearwifes", null, bearwife);
    }

    public synchronized void addLair(Lair lair) {
        lairs.add(lair);
        this.pcs.firePropertyChange("lairs", null, lair);
    }

    public synchronized void removeLair(Lair lair) {
        lairs.remove(lair);
        this.pcs.firePropertyChange("lairs", null, lair);
    }

    public synchronized void addBush(Bush bush) {
        bushes.add(bush);
        this.pcs.firePropertyChange("bushes", null, bush);
    }

    public synchronized void removeBush(Bush bush) {
        bushes.remove(bush);
        this.pcs.firePropertyChange("bushes", null, bush);
    }

    public synchronized Bear[] getBears() {
        return (Bear[])bears.toArray(new Bear[bears.size()]);
    }

    public synchronized Bearwife[] getBearwifes() {
        return (Bearwife[])bearwifes.toArray(new Bearwife[bearwifes.size()]);
    }

    public synchronized Lair[] getLairs() {
        return (Lair[])lairs.toArray(new Lair[lairs.size()]);
    }

    public synchronized Bush[] getBushes() {
        return (Bush[])bushes.toArray(new Bush[bushes.size()]);
    }

    public synchronized void clear() {
        bears.clear();
        bearwifes.clear();
        lairs.clear();
        bushes.clear();
        river = null;
        daytime = true;
    }

    //-------- property methods --------
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
