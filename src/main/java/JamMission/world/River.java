package jammission.world;


public class River extends LocationObject {

    protected String name;

    public River() {}

    public River(Location location) {
        this("River", location);
    }

    public River(String name, Location location) {
        setId(name);
        setName(name);
        setLocation(location);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "River(" + "id=" + getId() + ", location=" + getLocation() + ", name=" + getName() + ")";
    }
}
