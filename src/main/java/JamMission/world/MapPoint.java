package jammission.world;

import jadex.commons.SReflect;

/**
 *  A map point reflects how often the agent
 *  was near to this point.
 */
public class MapPoint extends LocationObject {
    protected int quantity;

    /** The seen value (1=just seen -> 0=never seen). */
    protected double seen;

    /**
     *  Bean constructor required for remote cleaner GUI.
     */
    public MapPoint() {}

    public MapPoint(Location location, int quantity, double seen) {
        super(location.toString(), location);
        this.quantity = quantity;
        this.seen = seen;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSeen() {
        return this.seen;
    }

    public void setSeen(double seen) {
        this.seen = seen;
    }

    public String toString() {
        return SReflect.getInnerClassName(this.getClass())
            +" loc: "+location+" quantity:"+quantity+" seen: "+seen;
    }

    /**
     *  Create a map point raster.
     */
    public static MapPoint[] getMapPointRaster(int numx, int numy, double width, double height) {
        double xwidth = width/(double)numx;
        double xstart = xwidth/2;
        double ywidth = height/(double)numy;
        double ystart = ywidth/2;

        MapPoint[] raster = new MapPoint[numx*numy];
        double yval = ystart;
        for(int y=0; y<numy; y++)
        {
            double xval = xstart;
            for(int x=0; x<numx; x++)
            {
                raster[y*numx+x] = new MapPoint(new Location(xval, yval), 0, 0);
                xval += xwidth;
            }
            yval += ywidth;
        }
        return raster;
    }
}
