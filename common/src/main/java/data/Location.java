package data;

import java.io.Serializable;

public class Location implements Serializable {
    private int x;
    private long y;
    private Integer z;

    public Location(int x, long y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Location) {
            Location locationObj = (Location) obj;
            return (x == locationObj.getX()) && (y == locationObj.getY()) && z.equals(locationObj.getZ());
        }
        return false;
    }
}
