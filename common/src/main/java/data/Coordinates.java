package data;


import java.io.Serializable;

public class Coordinates implements Serializable {
    private Long x;
    private Float y;

    public Coordinates(Long x, Float y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return X-coordinate.
     */
    public Long getX(){
        return x;
    }

    /**
     * @return Y-coordinate.
     */
    public Float getY() {
        return y;
    }

    @Override
    public String toString(){
        return "X:" + x + " Y:" + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Coordinates) {
            Coordinates coordinatesObj = (Coordinates) obj;
            return (x.equals(coordinatesObj.getX())) && y.equals(coordinatesObj.getY());
        }
        return false;
    }
}
