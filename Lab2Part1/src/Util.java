import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    public static double perimeter(Circle c) {return 2*Math.PI*c.getRadius();}
    public static double perimeter(Rectangle r) {
        return 2*(Math.abs(r.getTopLeft().getY()-r.getBottomRight().getY())) + 2*(Math.abs(r.getBottomRight().getX() - r.getTopLeft().getX()));
    }
    public static double perimeter(Polygon p) {
        ArrayList<Point> new_list = new ArrayList<>(p.getPoints().size());
        new_list.addAll(p.getPoints());
        double dis = 0;
        for(int i = 0; i < p.getPoints().size(); i++) {
            double dis_x = new_list.get((i+1) % p.getPoints().size()).getX() - new_list.get(i).getX();
            double dis_y = new_list.get((i+1) % p.getPoints().size()).getY() - new_list.get(i).getY();
            dis += Math.sqrt(Math.pow(dis_x, 2) + Math.pow(dis_y, 2));
        }
        return dis;
    }

}

