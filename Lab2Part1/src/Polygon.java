
import java.util.List;

public class Polygon {
    private final List<Point> poly;

    public Polygon(List<Point> poly) {
        this.poly = poly;
    }
    public List<Point> getPoints() {return poly;}
}
