public class Bigger {
    public static double whichIsBigger(Circle c, Rectangle r, Polygon p) {
        if (c.perimeter() > r.perimeter()) {
            if (c.perimeter() > p.perimeter()) {
                return c.perimeter();
            }
            return p.perimeter();
        }
        if (p.perimeter() > r.perimeter()) {
            return p.perimeter();
        }
        return r.perimeter();
    }
}
