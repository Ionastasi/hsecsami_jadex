package jammission.world;

import jadex.commons.SimplePropertyChangeSupport;
import jadex.commons.beans.PropertyChangeListener;

public abstract class LocationObject implements Cloneable {

    protected String id;
    protected Location location;
    protected SimplePropertyChangeSupport pcs;

    public LocationObject() {
        pcs = new SimplePropertyChangeSupport(this);
    }

    public LocationObject(String id, Location location) {
        pcs = new SimplePropertyChangeSupport(this);
        setId(id);
        setLocation(location);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        String oldid = this.id;
        this.id = id;
        pcs.firePropertyChange("id", oldid, id);
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        Location oldloc = this.location;
        this.location = location;
        pcs.firePropertyChange("location", oldloc, location);
    }

    public String toString() {
        return "LocationObject(" + "id=" + getId() + ", location=" + getLocation() + ")";
    }

    public boolean equals(Object o) {
        return o instanceof LocationObject && ((LocationObject)o).id.equals(id)
            && o.getClass().equals(this.getClass());
    }

    public int hashCode() {
        return id.hashCode();
    }

    public Object clone() {
        try {
            LocationObject clone = (LocationObject)super.clone();
            if(getLocation()!=null)
                clone.setLocation((Location)getLocation().clone());
            return clone;
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported");
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if(listener!=null)
            pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
