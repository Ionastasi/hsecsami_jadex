package jammission.world;

public class Location implements Cloneable {

    /** Distance, when two locations are considered near. */
    public static final double DEFAULT_TOLERANCE = 0.001;
    protected double x;
    protected double y;

    public Location() {}

    public Location(double x, double y) {
        setX(x);
        setY(y);
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "Location(" + "x=" + getX() + ", y=" + getY() + ")";
    }

    public double getDistance(Location other) {
        assert other != null;
        return Math.sqrt((other.y - this.y) * (other.y - this.y) + (other.x - this.x) * (other.x - this.x));
    }

    public boolean isNear(Location other) {
        return isNear(other, DEFAULT_TOLERANCE);
    }

    public boolean isNear(Location other, double tolerance) {
        return getDistance(other) <= tolerance;
    }

    public boolean equals(Object o) {
        if(o instanceof Location) {
            Location loc = (Location)o;
            if(loc.x == x && loc.y == y)
                return true;
        }
        return false;
    }

    public int hashCode() {
        return (int)(x * 21 + y);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch(CloneNotSupportedException e) {
            assert false;
            throw new RuntimeException("Clone not supported");
        }
    }
}
