import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class WorkSpace {
    private List<Shape> list = null;
    public WorkSpace() {
    }
    public void add(Shape shape) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(shape);}
    public Shape get(int index) { return list.get(index);}
    public int size() { return list.size();}
    public List<Circle> getCircles() {
        List<Circle> circles = new ArrayList<>();
        for(Shape s: list) {
            if (s instanceof Circle) {
                circles.add((Circle)s);
            }
        }
        return circles;
    }
    public List<Rectangle> getRectangles() {
        List<Rectangle> rectangles = new ArrayList<>();
        for(Shape s: list) {
            if (s instanceof Rectangle) {
                rectangles.add((Rectangle)s);
            }
        }
        return rectangles;
    }
    public List<Triangle> getTriangles() {
        List<Triangle> triangles = new ArrayList<>();
        for(Shape s: list) {
            if (s instanceof Triangle) {
                triangles.add((Triangle)s);
            }
        }
        return triangles;
    }
    public List<Shape> getShapesByColor(Color color) {
        List<Shape> shapes = new ArrayList<>();
        for(Shape s: list) {
            if (s.getColor().equals(color)) {
                shapes.add(s);
            }
        }
        return shapes;
    }
    public double getAreaOfAllShapes() {
        double area = 0.0;
        for (Shape s: list) {
            area += s.getArea();
        }
        return area;
    }
    public double getPerimeterOfAllShapes() {
        double per = 0.0;
        for (Shape s: list) {
            per += s.getPerimeter();
        }
        return per;
    }
}
