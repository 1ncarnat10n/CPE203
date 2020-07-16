import java.awt.Color;
import java.awt.Point;

public class Circle implements Shape {
    private double radius;
    private Point center;
    private Color color;
    public Circle(double radius, Point center, Color color) {
        this.radius = radius;
        this.center = center;
        this.color = color;
    }
    public Color getColor() { return color; }
    public void setColor(Color c) { color = c; }
    public double getArea() { return Math.PI*radius*radius;}
    public double getPerimeter() {return Math.PI*2*radius;}
    public void translate(Point p) {center = p;}
    public double getRadius() {return radius;}
    public void setRadius(double r) {radius = r;}
    public Point getCenter() {return center;}
    public boolean equals(Object O) {
        if (O.getClass() != this.getClass()) {return false;}
        Circle c = (Circle) O;
        if (this.radius == c.radius && this.center == c.center && this.color == c.color) {
            return true;
        }
        return false;
    }
}
