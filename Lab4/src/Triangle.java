import java.awt.Color;
import java.awt.Point;
public class Triangle implements Shape{
    private Point a;
    private Point b;
    private Point c;
    private Color color;
    public Triangle(Point a, Point b, Point c, Color color) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.color = color;
    }
    public Color getColor() { return color; }
    public void setColor(Color c) { color = c; }
    public double getArea() {
        double s = 0;
        s = (distance(a,b) + distance(b,c) + distance(c,a))/2;
        return Math.sqrt(s*(s - distance(a,b))*(s - distance(b,c))*(s - distance(c,a)));
    }
    public double getPerimeter() {
        return distance(a,b) + distance(b,c) + distance(c,a);}
    public void translate(Point p) {
        a.x += p.x;
        a.y += p.y;
        b.x += p.x;
        b.y += p.y;
        c.x += p.x;
        c.y += p.y;
    }
    private double distance(Point x, Point y) {
        return Math.sqrt(Math.pow(x.getX()-y.getX(), 2) + Math.pow(x.getY()-y.getY(), 2));
    }
    public Point getVertexA() { return a;}
    public Point getVertexB() {return b;}
    public Point getVertexC() {return c;}
    public boolean equals(Object O) {
        if (O.getClass() != this.getClass()) {return false;}
        Triangle t = (Triangle) O;
        if (this.a == t.a && this.b == t.b && this.c == t.c
                && this.color == t.color) {
            return true;
        }
        return false;
    }
}
