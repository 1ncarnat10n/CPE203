import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private final List<Point> poly;

    public Polygon(List<Point> poly) {
        this.poly = poly;
    }
    public List getPoints() {return poly;}
    public double perimeter() {
        ArrayList<Point> new_list = new ArrayList<>(getPoints().size());
        new_list.addAll(getPoints());
        double dis = 0;
        for(int i = 0; i < getPoints().size(); i++) {
            double dis_x = new_list.get((i+1) % getPoints().size()).getX() - new_list.get(i).getX();
            double dis_y = new_list.get((i+1) % getPoints().size()).getY() - new_list.get(i).getY();
            dis += Math.sqrt(Math.pow(dis_x, 2) + Math.pow(dis_y, 2));
        }
        return dis;
    }
}
