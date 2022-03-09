public class Vector {

    private final double xMag, yMag;

    public Vector(final double xMag, final double yMag){
        this.xMag = xMag;
        this.yMag = yMag;
    }

    public double getxMag() {
        return xMag;
    }

    public double getyMag() {
        return yMag;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "xMag=" + xMag +
                ", yMag=" + yMag +
                '}';
    }
}
