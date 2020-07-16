public class Point {
    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() { return z; }

    public double getRadius() {
        return Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
    }

    public double getAngle() {
        return Math.atan(y / x) + Math.toRadians(180.0);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Point{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
