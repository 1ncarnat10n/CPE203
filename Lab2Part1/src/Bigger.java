
public class Bigger {
    public static double whichIsBigger(Circle c, Rectangle r, Polygon p) {
        if (Util.perimeter(c) > Util.perimeter(r)) {
            if (Util.perimeter(c) > Util.perimeter(p)) {
                return Util.perimeter(c);
            }
            return Util.perimeter(p);
        }
        else {
            return Util.perimeter(r);
        }

    }
}
