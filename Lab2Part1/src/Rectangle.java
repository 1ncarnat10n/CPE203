public class Rectangle {
    private final Point top;
    private final Point bottom;

    public Rectangle(Point top, Point bottom) {
        this.top = top;
        this.bottom = bottom;
    }
    public Point getTopLeft() { return top;}
    public Point getBottomRight() {return bottom;}
}
