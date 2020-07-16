import java.awt.Color;
import java.awt.Point;
public class Rectangle implements Shape{
    private double width;
    private double height;
    private Point topLeft;
    private Color color;
    public Rectangle(double width, double height, Point topLeft, Color color) {
        this.width = width;
        this.height = height;
        this.topLeft = topLeft;
        this.color = color;
    }
    public Color getColor() { return color; }
    public void setColor(Color c) { color = c; }
    public double getArea() { return width*height;}
    public double getPerimeter() {return 2*width + 2*height;}
    public void translate(Point p) {topLeft = p;}
    public double getWidth() {return width;}
    public void setWidth(double w) {width = w;}
    public double getHeight() {return height;}
    public void setHeight(double h) {height = h;}
    public Point getTopLeft() {return topLeft;}
    public boolean equals(Object O) {
        if (O.getClass() != this.getClass()) {return false;}
        Rectangle r = (Rectangle) O;
        if (this.width == r.width && this.height == r.height && this.color == r.color
            && this.topLeft == r.topLeft) {
            return true;
        }
        return false;
    }
 }
